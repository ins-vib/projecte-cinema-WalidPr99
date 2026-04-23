# projecte-cinema-WalidPr99

Projecte **CinemaDAW** - sistema web de gestio de cines, sales, pellicules, projeccions i reserves dentrades amb un sistema de fidelitzacio per punts integrat.

Desenvolupat amb **Spring Boot 3.5**, **Thymeleaf**, **Spring Security**, **JPA/Hibernate** i **H2** (Java 17).

---

## Diagrama de classes (UML)

![Diagrama de classes UML](https://www.plantuml.com/plantuml/png/bLHDR-Cs43sRluB8SO4ijbSYA5QrrSA0emIssqDlj3Z84iiFWQGsTOl-ztB4YAReTOCzIKEycVVcO-Qp3zo5KIlYlqepSCSrxHNtlWd1oTCOODjRSv1_0_r4laW15FGGpbI3Fe7pX0GP5D2rDA2vuA_qdcuaVubXE1MGGa64JJzlCFeFMTrJAHZTMVE2_uPhOFGG6PFDXN3WVVdKot0kxS56vMfjnSBHMQjzZ2jzUDZ7_v_9lpCtckypztpWYKgQa7XHCdkJZYyUU9Wv3l6_u42pvaWEU_jQqfWHovT9_if_5-9m7e1D4O_nRueJU8XF9siLS3EdowRCBXeUxJS9roAmJujCnUXuaDPamXSmRea0tpiv951HOuZq7ZOy08RlkSBVeWYz0p0HV2r2HE1Hwj8FpQcVJlRnNTZnfAO7ZNcmbCwjNDTMSoFu3n456xXgj1rDAEbw9S64hXe7q5ogyW5xC7f6dvm0TvYCLDt-A-IfEA9LpaLwg6LMIS-Zafe-MNCbMIvg6jAKDflpln3zxi5TqupnjHxq8S9VhHFBxL609LP5DpjuJG6_01TAcaMGanLvAT45ubpdaKMtGq1zJf3LimCjHqrMZU13097XHINHuxdjDcrt9AlrK_VhTl_OR9huttJhTXT_inUAH7YpUTns4R3RebDMlRDdhiAvwNliy_ShmgPgfLiYJV1f4UO9EGom790WeyiS-2YJ9__6fU8HiP6-lnef1po0Q48zXLdcCn8V7JUUzxXcrqBL8X5M9VRTOSYtemYC_QWplRtXjuMdwfTLGCc_x9-wFzlOxUtkZtPFLhyzxJOPMwb0VDliOy_sxQPj7n6NRYkz--cEVhY_ftUVFdxyS9SEDaiddQ1H0TArPUaUatJs4F6G4SKfOkcUv3YtKCX6KYLhGVCMitdR2Gh8mMeGoY8vz6rONcmSwGgMzXbLAI1vhnAcBaF05fVBLC7cfDZR3IC4WsSKVgkjOTEQaEgrAaezmgZaOlb5BH_-psqUP58EL-Lr9J3W7jOJzYvLgXNv34QCMf7_0000)

---

## Diagrama de casos dus

![Diagrama de casos dus](https://www.plantuml.com/plantuml/png/ZL7BRjmm3BphAuISkeVq0ueWo290Yzw2fisTbRaE4LfoIMhx_lTQQw-xGhN5tcpD33cSkLC3iJomSumxyvQyKFzilYF1O9IYqnUA8mWCVeJm0Zq-sdT6Ns28FUC90O8bcM7uYfe6TExmvg-UI6dYsvK7zOUV94ViasOGyf0Der60cRJDzdnaRBk1ulnQlexFzqouZ9m_dIigV_Fh-dfXhlyhOpK-wIdY07uxZbS_dLjS-OdxwdsaG21UKPNIfi8-O4zg0dAjM2FtAMfcctGn6GL2zMF-pFGbesuMRuNtX5dGZoWxuitVvuyGmxfpUQ_7Xc8OjOcDo4mXynbOWo16Ylqf_0wrT5mcf_H2bVH1qXxLTxWQSeT8Q-5ggGagy5a8InqjiBgbXTV7kBdOas03Y24p2lXmm3R_SjQ26en7P0oXibdX3u9xqWnCzcEY22hA7YhATeSzhMeFN4m9rdlMUrhWMuh71I4DewppwruB2B53RTpOeG7pme06OOI84s577J8PMBq2ufvA5xbSCEAXpIurnZoJMX82dhbr42DJ0F4TQZd3y3GBTovA__gsNOD_uszkA0RE7TxUkhCvNpoXaVCaRNJZQrqpuY9jTJEByPjXx2QjEvjjqRUxQHfePF-_2RMBLZLLV8reBWpleltkjtDtqsSU-0y0)

---

## Funcionalitat extra: Sistema de fidelitzacio per punts

Sistema complet de punts per clients, integrat amb el proces de compra existent.

### Que fa

- Cada compra dona **1 punt per euro gastat** (acreditat automaticament al checkout).
- El client pot **canviar 100 punts per 5 euros** de saldo de descompte.
- El saldo acumulat es pot **aplicar a la seguent compra** mitjancant un checkbox al carret, que resta l'import del total real de la comanda.
- Cada client te un **nivell** segons el total gastat:
  - **BRONZE** (menys de 100 euros)
  - **SILVER** (entre 100 i 499 euros)
  - **GOLD** (500 euros o mes)
- Historial complet amb tots els moviments (punts guanyats i saldo canviat/aplicat).

### Arxius nous

| Capa | Fitxers |
|------|---------|
| Entitats | `LoyaltyAccount`, `PointTransaction`, `LoyaltyTier`, `TransactionType` |
| Repositoris | `LoyaltyAccountRepository`, `PointTransactionRepository` |
| Servei | `LoyaltyService` (logica de punts, tiers, canjes, aplicacio de descomptes) |
| Controlador | `LoyaltyController` (4 endpoints: `/loyalty`, `/loyalty/redeem` GET/POST, `/loyalty/history`) |
| Vistes | `templates/loyalty/panel.html`, `redeem.html`, `history.html` |

### Integracio amb el codi existent

- `SecurityConfig`: 1 linia per restringir `/loyalty/**` a rol `CLIENT`.
- `layout.html`: 1 enllac "Fidelitat" al menu de navegacio del client.
- `CartController`: injeccio de `LoyaltyService` + crida a `earnPointsForOrder` i `applyDiscountToOrder` dins del checkout (amb try-catch per no trencar el flux de compra).
- `client/cart.html`: checkbox "Aplicar X euros de descompte" just abans del boto de confirmar compra.

No s'ha modificat cap entitat existent (`User`, `Comanda`, `Ticket` ni `TicketService`).

### Requisits del professor que compleix

| Requisit | Com es cumpleix |
|----------|-----------------|
| Utilitat clara dins del sistema | Cicle real estalviar -> canviar -> aplicar descompte |
| Com a minim una entitat nova | 2 entitats (`LoyaltyAccount`, `PointTransaction`) + 2 enums |
| Persistencia a BD | Taules `loyalty_accounts` i `point_transactions` via JPA |
| Nous metodes en controllers | 4 endpoints nous a `LoyaltyController` |
| Interficie d'usuari | 3 vistes Thymeleaf + checkbox integrat al carret |
| Integracio amb entitats existents | `@OneToOne` amb `User`, `@ManyToOne` amb `Comanda` |
| Logica de negoci real | Calcul de punts, tiers, validacio de canjes, aplicacio de descompte, progres cap al seguent nivell |
| Relacions entre entitats | `@OneToOne` (User - LoyaltyAccount), `@ManyToOne` x2 (PointTransaction - LoyaltyAccount i Comanda) |
| Proces amb multiples passos | Comprar -> guanyar punts -> canviar tier -> canjar per saldo -> aplicar al carret -> veure historial |
