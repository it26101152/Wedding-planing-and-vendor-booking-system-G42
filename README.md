# Wedding Planning and Vendor Booking System (SE1020 OOP Project)

Spring Boot + JSP web app. Data is stored in plain text files in `/data` (no database).

## Run
1. Open the folder in IntelliJ IDEA as a Maven project (JDK 17).
2. Run `WeddingPlannerApplication`.
3. Open http://localhost:8080/ and you should see the home page.

## Package layout (one package per member = fewer merge conflicts)
```
com.weddingplanner
 ├─ common/        SHARED (do not edit without telling the group)
 │   ├─ model/BaseEntity            parent of every entity
 │   ├─ repository/FileRepository   generic text-file CRUD
 │   ├─ datastructure/CustomLinkedList
 │   ├─ util/ BubbleSort, FileUtil, Validator
 │   ├─ enums/ exception/ config/
 ├─ user/      Member 1      ├─ vendor/   Member 2
 ├─ wedding/   Member 3      ├─ booking/  Member 4
 ├─ payment/   Member 5      └─ guest/    Member 6
      each has: controller/ service/ repository/ model/
JSP files:  src/main/webapp/WEB-INF/views/<module>/*.jsp
```

## The six functions
| # | Function | CRUD | Data file | Entity extends BaseEntity |
|---|----------|------|-----------|---------------------------|
| 1 | User Management | Register, Login, Search/View, Update profile, Delete | users.txt | User |
| 2 | Vendor Management | Add, View/Search, Update, Delete + **Linked List** + **Bubble Sort by price** | vendors.txt | Vendor |
| 3 | Wedding Plan Management | Create plan, View, Update, Delete | weddings.txt | WeddingPlan |
| 4 | Vendor Booking Management | Book vendor, View, Update/Reschedule, Cancel | bookings.txt | Booking |
| 5 | Payment & Invoice Management | Record payment, View, Update, Delete/Refund | payments.txt | Payment |
| 6 | Guest List & RSVP Management | Add guest, View/Search, Update RSVP, Delete | guests.txt | Guest |

Records link to each other by id: `Wedding.userId`, `Booking.weddingId + vendorId`,
`Payment.bookingId`, `Guest.weddingId`.

## How to write your module (example for Vendor)
```java
public class Vendor extends BaseEntity {
    private String name; private VendorCategory category; private double price;
    // getters/setters ...
    @Override public String[] toFields() {
        return new String[]{name, category.name(), String.valueOf(price)};
    }
}

@Repository
public class VendorRepository extends FileRepository<Vendor> {
    public VendorRepository(@Value("${app.data.dir}") String dir) { super(dir, "vendors.txt", "VND"); }
    @Override protected Vendor fromFields(String[] f) {
        Vendor v = new Vendor(); v.setName(f[0]); v.setCategory(VendorCategory.valueOf(f[1]));
        v.setPrice(Double.parseDouble(f[2])); return v;
    }
}
```
`save()` = create/update, `findAll()/findById()` = read, `deleteById()` = delete.

## Git rules
- `main` is protected: nobody commits directly after the first push.
- One branch per member: `feature/user`, `feature/vendor`, `feature/wedding`, `feature/booking`, `feature/payment`, `feature/guest`.
- Commit small and often with clear messages (the commit history is marked in the report and viva).
- Open a Pull Request into `main`; another member reviews it.
- Never commit changes to `common/` on your own branch without telling the group.
