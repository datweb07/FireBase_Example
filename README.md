![Alt Text](Firestore.gif)

## Theo dõi lượt xem

Mỗi lần nhấn một bài viết để mở trang chi tiết, ứng dụng tăng nguyên tử trường `view` của document tương ứng trong collection `articles` thêm 1. Trường này có kiểu Number. Document cũ chưa có `view` không cần chỉnh thủ công: lần tăng đầu tiên tạo trường với giá trị `1`.

Nếu muốn thêm sẵn trường trong Firebase Console: mở **Firestore Database → Data → articles → chọn document → Add field**, nhập tên `view`, chọn kiểu **number**, nhập `0`, rồi lưu. Lặp lại cho các document muốn khởi tạo trước.

Đảm bảo Firestore Rules cho phép ứng dụng cập nhật document `articles`; nếu không, bài viết vẫn mở nhưng ứng dụng sẽ báo lỗi cập nhật lượt xem.
