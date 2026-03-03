# variables

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=barilo-egor_variables&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=barilo-egor_variables)

Микросервис variables для единой точки хранения переменных.

Добавлена возможность хранения оптовых скидок. Сервис отправляет ивенты в кафка топик при обновлении сделок, а также при старте приложения. Доступна возможность отключения отправления ивента при старте.
Получение актуальных скидок происходит посредством grpc.

Для работы требуется `config/config.yml` рядом c jar архивом следующей структуры:

```yaml
spring:
  port:
  datasource:
    username:
    password:
    url:
    driver-class-name:
  jpa:
    hibernate:
      ddl-auto:
  grpc:
    server:
      host:
      port:
  kafka:
    bootstrap-servers:
kafka:
  topic:
    bulk-discount: # название топика, в который будут отправляться ивенты обновления оптовых скидок
variables:
  bulk-discount:
    send-all-after-start: true # отправлять ли ивент обновления со всеми скидками при старте
