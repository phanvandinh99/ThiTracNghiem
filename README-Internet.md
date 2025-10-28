## Hướng dẫn triển khai cho khách hàng dùng qua Internet
Mục tiêu: triển khai máy chủ (server) ở một nơi, khách ở mọi nơi có thể kết nối làm bài.

### Tổng quan
- Ứng dụng gồm 2 phần:
  - Server (Java, cổng 5000) + MySQL (lưu câu hỏi/kết quả) đặt ở 1 máy chủ có IP Public hoặc sau router có cấu hình Port Forwarding.
  - Client (Java Swing) chạy ở máy người dùng, kết nối tới Server qua Internet.
- Dữ liệu đồng nhất vì tất cả client ghi/đọc cùng 1 CSDL trên server.

---

### 1) Chuẩn bị máy chủ (Server)
- Hệ điều hành Windows (hoặc Linux) có thể mở cổng ra Internet.
- Cài đặt:
  - Java Runtime (JRE) 8+ hoặc JDK 8+.
  - MySQL 5.7/8 (dịch vụ MySQL chạy thường trực).
- Có 1 trong 2 điều kiện sau:
  - Máy chủ có IP Public tĩnh/động, hoặc
  - Máy chủ nằm sau router: cấu hình Port Forwarding cổng TCP 5000 → IP nội bộ của máy chủ (ví dụ 192.168.1.7).

#### 1.1 Cài Java và MySQL
- Cài JRE/JDK 8+ (Oracle/OpenJDK đều được).
- Cài MySQL, ghi nhớ tài khoản root hoặc tạo user chuyên dụng (khuyến nghị, xem dưới).

#### 1.2 Tạo cơ sở dữ liệu và bảng
Đăng nhập MySQL và chạy:
```sql
CREATE DATABASE IF NOT EXISTS hoidap_db CHARACTER SET utf8mb4;
USE hoidap_db;

CREATE TABLE IF NOT EXISTS cauhoi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  noidung VARCHAR(1000) NOT NULL,
  dapan_a VARCHAR(255) NOT NULL,
  dapan_b VARCHAR(255) NOT NULL,
  dapan_c VARCHAR(255) NOT NULL,
  dapan_d VARCHAR(255) NOT NULL,
  dapandung ENUM('A','B','C','D') NOT NULL
) ENGINE=InnoDB;

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
Thêm dữ liệu mẫu (tuỳ chọn):
```sql
INSERT INTO cauhoi (noidung, dapan_a, dapan_b, dapan_c, dapan_d, dapandung) VALUES
('2 + 2 = ?', '3', '4', '5', '6', 'B'),
('Java collection nào cho key->value?', 'List', 'Set', 'Map', 'Queue', 'C');
```

#### 1.3 Tạo tài khoản DB riêng (khuyến nghị)
```sql
CREATE USER 'appuser'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hoidap_db.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
```
Lưu ý: Không khuyến nghị mở cổng MySQL (3306) ra Internet cho client. Ứng dụng của bạn lắng nghe cổng 5000 và client sẽ kết nối vào đó; server sẽ truy cập DB nội bộ.

#### 1.4 Cấu hình ứng dụng (config.properties)
- Copy `config.properties.sample` → `config.properties` và đặt cạnh file JAR trong thư mục `dist/`.
- Chỉnh sửa giá trị (nếu MySQL chạy trên cùng máy server app, dùng `localhost`):
```
db.url=jdbc:mysql://localhost/hoidap_db?serverTimezone=UTC&useSSL=false
db.user=appuser
db.password=your_password
```

#### 1.5 Build và chuẩn bị thư mục chạy
- Mở dự án trong NetBeans → Add `mysql-connector-j-*.jar` vào Libraries.
- Clean and Build → sinh `dist/ThiTracNghiem.jar` và `dist/lib/`.
- Đảm bảo `config.properties` nằm cạnh `dist/ThiTracNghiem.jar`.
- Mở Windows Firewall cho cổng 5000:
  - Windows Security → Firewall & network protection → Advanced settings → Inbound Rules → New Rule → Port → TCP → 5000 → Allow.

#### 1.6 Cấu hình Router (nếu chạy sau router)
- Tìm IP nội bộ của server (ví dụ `192.168.1.7`).
- Vào trang cấu hình router → Port Forwarding/Virtual Server:
  - Protocol: TCP
  - External Port: 5000
  - Internal IP: 192.168.1.7
  - Internal Port: 5000
- Lưu, khởi động lại router nếu cần.

#### 1.7 Chạy server
- Trong thư mục dự án, nhấp đôi `run-server.bat` (hoặc chạy lệnh tương đương):
```
java -cp "dist/ThiTracNghiem.jar;dist/lib/*" server.Server
```
- Ghi nhớ IP Public (tìm “what is my ip”).

---

### 2) Chuẩn bị máy khách (Client qua Internet)
- Cài Java Runtime (JRE) 8+.
- Nhận từ bạn (qua zip/drive): thư mục `dist/` và file `run-client.bat`.
- Chạy `run-client.bat` → nhập:
  - Host: IP Public hoặc tên miền của máy chủ.
  - Port: `5000`.

Kiểm tra kết nối nếu gặp lỗi:
- PowerShell: `Test-NetConnection your.public.ip -Port 5000`.
- Nếu không thông: kiểm tra Port Forwarding và Firewall ở máy chủ.

---

### 3) Quy trình kiểm thử nhanh (checklist)
- [ ] MySQL chạy, bảng đã tạo, tài khoản DB hoạt động.
- [ ] `dist/ThiTracNghiem.jar` và `dist/lib/mysql-connector-j-*.jar` tồn tại.
- [ ] `config.properties` đặt cạnh JAR, giá trị đúng.
- [ ] Server chạy `run-server.bat` không báo lỗi.
- [ ] Trên router: Port Forwarding TCP 5000 đến IP nội bộ máy server.
- [ ] Windows Firewall đã mở cổng 5000 (Inbound).
- [ ] Client ngoài Internet Test-NetConnection tới IP Public port 5000 thành công.

---

### 4) Bảo mật và độ ổn định (khuyến nghị)
- Không mở cổng 3306 (MySQL) ra Internet cho client.
- Đặt mật khẩu mạnh cho `appuser`. Giới hạn IP nếu có thể.
- Sao lưu CSDL định kỳ.
- Cân nhắc dùng tên miền động (Dynamic DNS) nếu IP Public thay đổi.
- Cân nhắc dùng VPN hoặc TLS nếu môi trường yêu cầu bảo mật cao.

---

### 5) Khắc phục lỗi thường gặp
- Client không kết nối được server:
  - Server chưa chạy hoặc sai IP/port.
  - Router chưa forward port 5000.
  - Firewall chặn cổng 5000.
- Lỗi DB (không tải câu hỏi/không ghi kết quả):
  - Sai `config.properties`.
  - MySQL chưa chạy hoặc thiếu bảng.
  - Quyền user DB chưa được cấp.
- Thiếu JDBC:
  - Kiểm tra `dist/lib/` có `mysql-connector-j-*.jar`.

---

### 6) Gợi ý quản trị vận hành
- Gửi cho khách hàng bộ cài gồm: `dist/`, `run-client.bat`, hướng dẫn nhập IP/port.
- Khi đổi server IP/port: chỉ cần báo lại khách hàng, không cần phát hành JAR mới.
- Khi nâng cấp server: build lại, thay `dist/` trên máy chủ, giữ nguyên `config.properties`.
