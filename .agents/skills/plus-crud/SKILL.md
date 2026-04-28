---
name: plus-crud
description: 'Estándares y directrices para la implementación de una API RESTful de Productos con persistencia políglota y Clean Architecture.'
---

# Plus CRUD Implementation Rules

- **Dominio Aislado:** Define el modelo de dominio `Product` (id, name, description, sku, price, stock, category, active, createdAt, updatedAt) sin acoplarlo a anotaciones de frameworks de persistencia.
- **Contrato de Repositorio:** Define una interfaz `ProductRepository` que establezca las operaciones CRUD básicas.
- **Persistencia:** Implementa repositorios específicos tecnológicos como `SqlServerProductRepository` (usando JPA/Hibernate), la base de datos se llamara `soft-pos` y la tabla `products`.
- **Uso de DTOs:** Nunca expongas la entidad de dominio. Utiliza `CreateProductRequest`, `UpdateProductRequest` y `ProductResponse` para la transferencia de datos.
- **Mapeo Manual:** Implementa transformaciones manuales (mappers) para convertir los objetos de request al dominio y del dominio al response, asegurando el control total sobre la mutación de datos.
- **Contrato REST:** Expón los endpoints bajo la ruta `/api/v1/products` (POST, GET por ID, GET listado, PUT, DELETE).