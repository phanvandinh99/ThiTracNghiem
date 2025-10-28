## ThiTracNghiem (Java Swing + Socket + MySQL)

Ứng dụng thi trắc nghiệm đơn giản theo mô hình client/server:
- Client: Giao diện Java Swing để làm bài thi
- Server: Server socket Java (cổng 5000)
- Cơ sở dữ liệu: MySQL qua JDBC

### Cấu trúc dự án
- `src/client`: Các form Swing cho đăng nhập/kết nối, thông tin thí sinh, thi, kết quả, đăng ký
- `src/server`: Server socket và các lớp trợ giúp JDBC
- `build.xml`: Tập tin build của NetBeans/Ant

### Yêu cầu môi trường
- Java 8+ (JDK)
- MySQL 5.7+ hoặc 8+
- MySQL JDBC driver (`mysql-connector-j`)
- NetBeans/Ant (khuyến nghị)

### Thiết lập cơ sở dữ liệu
1) Tạo database:
```sql
CREATE DATABASE IF NOT EXISTS hoidap_db CHARACTER SET utf8mb4;
USE hoidap_db;
```

2) Tạo bảng (theo cách mà mã hiện tại đang sử dụng):
```sql
-- Bảng câu hỏi
CREATE TABLE IF NOT EXISTS cauhoi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  noidung VARCHAR(1000) NOT NULL,
  dapan_a VARCHAR(255) NOT NULL,
  dapan_b VARCHAR(255) NOT NULL,
  dapan_c VARCHAR(255) NOT NULL,
  dapan_d VARCHAR(255) NOT NULL,
  dapandung ENUM('A','B','C','D') NOT NULL
) ENGINE=InnoDB;

-- Bảng kết quả
CREATE TABLE IF NOT EXISTS ketqua (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  mssv VARCHAR(50) NOT NULL,
  soCauDung INT NOT NULL,
  tongCau INT NOT NULL,
  diem DECIMAL(4,2) NOT NULL,
  ngayThi DATETIME NULL,
  thoigianthi DATETIME NULL
) ENGINE=InnoDB;
```

3) Thêm dữ liệu mẫu (tuỳ chọn):
```sql
INSERT INTO cauhoi (noidung, dapan_a, dapan_b, dapan_c, dapan_d, dapandung) VALUES
('2 + 2 = ?', '3', '4', '5', '6', 'B'),
('Java collection nào cho key->value?', 'List', 'Set', 'Map', 'Queue', 'C');
```

### Cấu hình kết nối CSDL
Chỉnh trong `src/server/DBConnection.java`:
```java
Class.forName("com.mysql.cj.jdbc.Driver");
String URL = "jdbc:mysql://localhost/hoidap_db?user=root&password=";
Connection con = DriverManager.getConnection(URL);
```
- Sửa `user` và `password` theo thông tin MySQL của bạn.
- Nếu dùng MySQL 8, có thể cần thêm `serverTimezone=UTC&useSSL=false` vào URL.

#### Dùng file cấu hình để giảm cài đặt
- Có sẵn `config.properties.sample`. Sao chép thành `config.properties` và chỉnh sửa:
```
# config.properties
db.url=jdbc:mysql://<IP-hoặc-host>/hoidap_db?serverTimezone=UTC&useSSL=false
db.user=appuser
db.password=your_password
```
- Ứng dụng sẽ tự đọc `config.properties` cùng thư mục chạy JAR, không cần build lại khi đổi máy/IP.

### Build và chạy
#### Cách A: NetBeans
- Mở thư mục dự án trong NetBeans.
- Đảm bảo JDBC JAR của MySQL có trong Libraries của project.
- Chạy `server.Server` (server lắng nghe cổng 5000).
- Chạy `client.formClient` (nhập host/port để kết nối), hoặc mở `client.formThongTin` sau khi kết nối thành công.

#### Cách B: Ant (CLI)
Nếu đã có Ant và JDBC JAR:
```bash
ant clean build
```
Chạy server (từ thư mục gốc dự án):
```bash
java -cp "build/classes;path/to/mysql-connector-j.jar" server.Server
```
Chạy client:
```bash
java -cp "build/classes;path/to/mysql-connector-j.jar" client.formClient
```
Lưu ý: Trên Linux/macOS dùng dấu `:` thay cho `;` trong classpath.

#### Cách nhanh trên Windows (khuyên dùng)
1) Trong NetBeans: Clean and Build để tạo thư mục `dist/`.
2) Đặt `config.properties` cạnh `dist/ThiTracNghiem.jar` (copy từ `config.properties.sample` và điền thông tin).
3) Nhấp đôi `run-server.bat` (chạy server từ `dist/ThiTracNghiem.jar` và `dist/lib/*`).
4) Nhấp đôi `run-client.bat` (chạy client từ cùng thư mục `dist/`).
5) Đảm bảo:
   - MySQL đã chạy và CSDL đã tạo như phần trên.
   - `mysql-connector-j-*.jar` nằm trong `dist/lib/` (NetBeans tự copy sau khi build nếu đã thêm vào Libraries).
   - Ở client, nhập đúng IP máy chạy server và port `5000`.

### Cách sử dụng
1) Khởi động server.
2) Khởi động client, nhập host (vd: `127.0.0.1`) và port `5000` để kết nối.
3) Vào màn hình làm bài, chọn đáp án cho từng câu, và nhấn NỘP.
4) Kết quả được lưu vào bảng `ketqua` và hiển thị ở form kết quả.

### Ghi chú và hạn chế hiện tại
- `ServerThread` hiện chưa xử lý dữ liệu đề/đáp án qua socket; client đang trực tiếp truy vấn/ghi MySQL. Nếu muốn đúng mô hình client‑server, hãy chuyển tất cả thao tác DB sang server và định nghĩa giao thức trao đổi qua socket.
- `formThi` đang ghép chuỗi SQL trực tiếp; nên chuyển sang `PreparedStatement` để tránh SQL injection và lỗi format.
- Khi chèn kết quả, mã đang dùng `NOW()` cho `ngayThi/thoigianthi`; nếu cần lưu thời gian do người dùng nhập, hãy thay thế tương ứng.
- Đảm bảo chỉ mở form kết quả một lần sau khi insert thành công.
- Thông tin kết nối DB đang hardcode; nên cấu hình hoá qua file/tham số môi trường.

### .gitignore
Đã kèm `.gitignore` để loại trừ thư mục build, file IDE và tệp hệ thống.

### Giấy phép
Vui lòng thêm giấy phép bạn muốn (ví dụ: MIT).
