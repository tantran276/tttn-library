insert into role values
(1, "ADMIN"),
(2, "USER");
insert into category values 
("000", "Tin học, thông tin & tác phẩm tổng quát"),
("100", "Triết học, cận tâm lý và thuyết huyền bí và tâm lý học"),
("200", "Tôn giáo"),
("300", "Khoa học xã hội"),
("400", "Ngôn ngữ"),
("500", "Khoa học tự nhiên và toán học"),
("600", "Công nghê (Khoa học ứng dụng)"),
("700", "Nghệ thuật & Mỹ thuật và trang trí"),
("800", "Văn học(Văn chương) và tu từ học"),
("900", "Lịch sử địa lý và các ngành phụ trợ");
insert into tag (name) values
("Kinh tế vận tải"),
("Công trình giao thông"),
("Cơ khí"),
("Kinh tế xây dựng"),
("Logistics và Quản lý chuổi cung ứng"),
("Công nghệ thông tin"),
("Java"),
("Tiếng Việt"),
("Bí quyết thành công");
insert into author (name) values
("Cay S. Horstmann"),
("Gary Cornell"),
("Nguyễn Viết Trung"),
("Ken Langdon"),
("Đào Huy Bích"),
("Thomas Lamb"),
("Đỗ Sanh"),
("Trường Đại học Xây dựng"),
("Phạm Văn Minh"),
("Trương Trọng Vũ"),
("Phạm Quang Huy"),
("Võ Duy Thanh Tâm"),
("William Stallings"),
("Spencer Johnson"),
("Ph. Ăng Ghen"),
("Robert Thurman"),
("VN-GUIDE"),
("John C. Maxwell");
insert into book(isbn, title, publisher_id, category_id, image, content, price, status, create_date) values
("1234567891111", "3500 địa chỉ Internet",1,"000",LOAD_FILE('/var/lib/mysql-files/default.png'), "Sách cung cấp 3500 địa chỉ Internet về Công nghệ thông tin; Khoa học kỹ thuật; Văn hóa xã hội; Giáo dục - đào tạo; Y học; Âm nhạc; Giải trí", 37000, 1,"2000-01-01");
insert into publisher(id,name) values
(1,"H.: Thống kê "),
(2, " Huế : Thuận Hóa");
insert into book_author values 
(1,17);





