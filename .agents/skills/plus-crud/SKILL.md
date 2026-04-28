---
name: plus-crud
description: 'Estándares y directrices detalladas para la implementación de una API RESTful de Productos bajo principios de Clean Architecture y persistencia relacional (SQL Server).'
---

# Plus CRUD Implementation Rules

Esta skill define el flujo de trabajo estandarizado paso a paso para la construcción de la API de productos. Sigue esta secuencia de implementación rigurosamente para garantizar el desacoplamiento, la mantenibilidad y la calidad del código.

## 1. Definición del Dominio Aislado (Core)
El núcleo de la aplicación no debe tener dependencias de frameworks externos (ni Spring Data, ni Hibernate, ni validadores de Jakarta).
- **Modelo de Dominio:** Crea la clase pura `Product` (POJO/Record).
- **Atributos obligatorios:** `UUID id`, `String name`, `String description`, `String sku`, `BigDecimal price`, `Integer stock`, `String category`, `Boolean active`, `LocalDateTime createdAt`, `LocalDateTime updatedAt`.
- **Regla estricta:** Prohibido el uso de anotaciones como `@Entity` o `@Table` en esta capa. Las validaciones de estado internas (ej. descontar stock) deben residir como métodos de esta clase.

## 2. Contrato de Repositorio (Puertos)
Define cómo el dominio interactúa con la persistencia sin conocer los detalles de implementación.
- **Interfaz:** Crea `ProductRepository` dentro de la capa de dominio.
- **Operaciones requeridas:**
  - `Optional<Product> findById(UUID id)`
  - `List<Product> findAll()`
  - `Product save(Product product)`
  - `void deleteById(UUID id)`
  - `boolean existsBySku(String sku)`

## 3. Implementación de Persistencia (Adaptadores)
Implementación concreta utilizando la tecnología requerida.
- **Base de Datos y Tabla:** El sistema se conectará a la base de datos `soft-pos` y administrará la tabla `products`.
- **Entidad de Persistencia:** Crea la clase `ProductJpaEntity` en la capa de infraestructura. Aquí **sí** debes utilizar las anotaciones de JPA (`@Entity`, `@Table(name = "products")`, `@Id`, `@Column`).
- **Implementación Concreta:** Crea la clase `SqlServerProductRepository` que implemente la interfaz `ProductRepository` del dominio. Esta clase encapsulará el uso de `CrudRepository` o `JpaRepository` de Spring Data.

## 4. Objetos de Transferencia de Datos (DTOs)
Protege la capa de dominio asegurando que la entidad nunca se exponga en los endpoints REST.
- **`CreateProductRequest`:** Campos estrictamente necesarios para la creación (`name`, `description`, `sku`, `price`, `stock`, `category`). Aplica validaciones del lado del framework (ej. `@NotBlank`, `@PositiveOrZero`).
- **`UpdateProductRequest`:** Campos permitidos para modificación. (Considera si el SKU debe ser inmutable).
- **`ProductResponse`:** Formato estandarizado de salida que incluye todos los datos públicos del producto, junto con su `id` y las fechas de auditoría (`createdAt`, `updatedAt`).

## 5. Mapeo Manual Explicito
Para garantizar control total, evitar comportamientos ocultos y facilitar el debugging, se prohíbe el uso de librerías de auto-mapeo (como MapStruct o ModelMapper).
- **Clase Mapper:** Crea un componente `ProductMapper` encargado de todas las transformaciones.
- **Transformaciones a implementar:**
  - *Request a Dominio:* `toDomain(CreateProductRequest request)`
  - *Dominio a Response:* `toResponse(Product product)`
  - *Dominio a Entidad JPA:* `toEntity(Product product)`
  - *Entidad JPA a Dominio:* `toDomain(ProductJpaEntity entity)`

## 6. Casos de Uso / Servicios (Aplicación)
Orquesta la lógica de negocio conectando el dominio con el repositorio.
- **Servicio:** Crea la clase `ProductService` e inyecta la interfaz `ProductRepository` (nunca la implementación concreta de JPA).
- **Reglas a aplicar:**
  - **Creación:** Validar que el SKU no exista llamando a `existsBySku`. Setear `active = true` y asignar `createdAt` por defecto.
  - **Actualización/Eliminación:** Verificar previamente la existencia del registro. Lanzar una excepción de negocio si no se encuentra (ej. `ProductNotFoundException`).

## 7. Contrato REST (Controladores)
Exposición de la API siguiendo convenciones estrictas de REST.
- **Ruta Base:** `@RequestMapping("/api/v1/products")`
- **Endpoints obligatorios:**
  - `POST /` : Recibe `CreateProductRequest`. Retorna código HTTP `201 Created` y el `ProductResponse`.
  - `GET /{id}` : Retorna código HTTP `200 OK` con el detalle del producto.
  - `GET /` : Retorna código HTTP `200 OK` con la lista de productos.
  - `PUT /{id}` : Recibe `UpdateProductRequest`. Retorna código HTTP `200 OK` con la información actualizada.
  - `DELETE /{id}` : Desactiva o elimina físicamente el registro. Retorna código HTTP `204 No Content`.

## 8. Manejo Global de Excepciones
Garantiza que el cliente reciba respuestas HTTP legibles y estandarizadas ante errores.
- **Controller Advice:** Implementa un `@RestControllerAdvice` o interceptor equivalente.
- **Mapeo de Errores:**
  - Errores de validación en DTOs -> `400 Bad Request`.
  - Producto no encontrado -> `404 Not Found`.
  - Violaciones de negocio (ej. SKU ya registrado) -> `409 Conflict`.
  - Errores internos de infraestructura -> `500 Internal Server Error`.