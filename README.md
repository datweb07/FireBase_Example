# Article Firebase

Ứng dụng Android mẫu lưu và hiển thị bài viết bằng Cloud Firestore.

## Cấu trúc dữ liệu

Mỗi bài viết được lưu tại `articles/{id}` với bốn trường kiểu chuỗi:

- `id`: mã bài viết
- `title`: tiêu đề
- `image`: URL ảnh HTTPS trên Internet (không tải ảnh lên Firebase Storage)
- `description`: nội dung bài viết

Đặt file cấu hình Firebase của Android tại `app/google-services.json`, sau đó chạy ứng dụng. Màn hình đầu dùng để thêm bài viết; nút **Xem danh sách bài viết** mở danh sách realtime. Chạm vào một bài viết để xem chi tiết và dùng nút **Quay lại** để trở về.

![Firestore demo](Firestore.gif)
