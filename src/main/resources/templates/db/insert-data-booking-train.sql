INSERT INTO airports (airport_code, airport_name, city, country) VALUES
('HN', 'Ga Hà Nội', 'Hà Nội', 'Vietnam'),
('SG', 'Ga Sài Gòn', 'TP. Hồ Chí Minh', 'Vietnam'),
('DN', 'Ga Đà Nẵng', 'Đà Nẵng', 'Vietnam'),
('HP', 'Ga Hải Phòng', 'Hải Phòng', 'Vietnam'),
('HUE', 'Ga Huế', 'Thừa Thiên Huế', 'Vietnam'),
('VT', 'Ga Vinh', 'Nghệ An', 'Vietnam'),
('NT', 'Ga Nha Trang', 'Khánh Hòa', 'Vietnam'),
('QN', 'Ga Quảng Ngãi', 'Quảng Ngãi', 'Vietnam'),
('BD', 'Ga Biên Hòa', 'Đồng Nai', 'Vietnam'),
('DT', 'Ga Dĩ An', 'Bình Dương', 'Vietnam'),
('QNI', 'Ga Quy Nhơn', 'Bình Định', 'Vietnam'),
('TH', 'Ga Thanh Hóa', 'Thanh Hóa', 'Vietnam'),
('NB', 'Ga Ninh Bình', 'Ninh Bình', 'Vietnam'),
('PT', 'Ga Phan Thiết', 'Bình Thuận', 'Vietnam'),
('DL', 'Ga Đà Lạt', 'Lâm Đồng', 'Vietnam'),
('VL', 'Ga Vinh Long', 'Vĩnh Long', 'Vietnam'),
('CT', 'Ga Cần Thơ', 'Cần Thơ', 'Vietnam'),
('QB', 'Ga Quảng Bình', 'Quảng Bình', 'Vietnam'),
('TN', 'Ga Thái Nguyên', 'Thái Nguyên', 'Vietnam'),
('LC', 'Ga Lào Cai', 'Lào Cai', 'Vietnam');

-- CHUYẾN TÀU HÀ NỘI - SÀI GÒN (15-30/11)
INSERT INTO flights (flight_number, airline, departure_time, arrival_time, departure_airport_id, arrival_airport_id, seat_capacity, price, user_id) VALUES
-- Ngày 15/11
('SE1', 'Đường Sắt Việt Nam', '2025-11-15 06:00:00', '2025-11-16 11:30:00', 1, 2, 400, 1200000, NULL),
('SE3', 'Đường Sắt Việt Nam', '2025-11-15 19:30:00', '2025-11-16 23:00:00', 1, 2, 400, 1200000, NULL),
-- Ngày 16/11
('SE5', 'Đường Sắt Việt Nam', '2025-11-16 06:00:00', '2025-11-17 11:30:00', 1, 2, 400, 1200000, NULL),
('SE7', 'Đường Sắt Việt Nam', '2025-11-16 22:00:00', '2025-11-18 04:30:00', 1, 2, 400, 1200000, NULL),
-- Ngày 18/11
('SE9', 'Đường Sắt Việt Nam', '2025-11-18 06:00:00', '2025-11-19 11:30:00', 1, 2, 400, 1200000, NULL),
-- Ngày 20/11
('SE11', 'Đường Sắt Việt Nam', '2025-11-20 06:00:00', '2025-11-21 11:30:00', 1, 2, 400, 1200000, NULL),
('SE13', 'Đường Sắt Việt Nam', '2025-11-20 19:30:00', '2025-11-21 23:00:00', 1, 2, 400, 1200000, NULL),
-- Ngày 22/11
('SE15', 'Đường Sắt Việt Nam', '2025-11-22 06:00:00', '2025-11-23 11:30:00', 1, 2, 400, 1200000, NULL),
-- Ngày 25/11
('SE17', 'Đường Sắt Việt Nam', '2025-11-25 06:00:00', '2025-11-26 11:30:00', 1, 2, 400, 1200000, NULL),
('SE19', 'Đường Sắt Việt Nam', '2025-11-25 22:00:00', '2025-11-27 04:30:00', 1, 2, 400, 1200000, NULL),

-- CHUYẾN TÀU SÀI GÒN - HÀ NỘI (CHIỀU NGƯỢC LẠI)
-- Ngày 17/11
('SE2', 'Đường Sắt Việt Nam', '2025-11-17 06:00:00', '2025-11-18 11:30:00', 2, 1, 400, 1200000, NULL),
('SE4', 'Đường Sắt Việt Nam', '2025-11-17 19:30:00', '2025-11-18 23:00:00', 2, 1, 400, 1200000, NULL),
-- Ngày 19/11
('SE6', 'Đường Sắt Việt Nam', '2025-11-19 06:00:00', '2025-11-20 11:30:00', 2, 1, 400, 1200000, NULL),
('SE8', 'Đường Sắt Việt Nam', '2025-11-19 22:00:00', '2025-11-21 04:30:00', 2, 1, 400, 1200000, NULL),
-- Ngày 21/11
('SE10', 'Đường Sắt Việt Nam', '2025-11-21 06:00:00', '2025-11-22 11:30:00', 2, 1, 400, 1200000, NULL),
-- Ngày 23/11
('SE12', 'Đường Sắt Việt Nam', '2025-11-23 06:00:00', '2025-11-24 11:30:00', 2, 1, 400, 1200000, NULL),
('SE14', 'Đường Sắt Việt Nam', '2025-11-23 19:30:00', '2025-11-24 23:00:00', 2, 1, 400, 1200000, NULL),
-- Ngày 26/11
('SE16', 'Đường Sắt Việt Nam', '2025-11-26 06:00:00', '2025-11-27 11:30:00', 2, 1, 400, 1200000, NULL),
-- Ngày 28/11
('SE18', 'Đường Sắt Việt Nam', '2025-11-28 06:00:00', '2025-11-29 11:30:00', 2, 1, 400, 1200000, NULL),
('SE20', 'Đường Sắt Việt Nam', '2025-11-28 22:00:00', '2025-11-30 04:30:00', 2, 1, 400, 1200000, NULL),

-- CÁC TUYẾN TÀU KHÁC
-- Hà Nội - Đà Nẵng
('SE21', 'Đường Sắt Việt Nam', '2025-11-16 07:00:00', '2025-11-16 21:00:00', 1, 3, 400, 600000, NULL),
('SE22', 'Đường Sắt Việt Nam', '2025-11-18 07:00:00', '2025-11-18 21:00:00', 1, 3, 400, 600000, NULL),
('SE23', 'Đường Sắt Việt Nam', '2025-11-22 07:00:00', '2025-11-22 21:00:00', 1, 3, 400, 600000, NULL),

-- Đà Nẵng - Sài Gòn
('SE24', 'Đường Sắt Việt Nam', '2025-11-17 08:00:00', '2025-11-17 20:00:00', 3, 2, 400, 500000, NULL),
('SE25', 'Đường Sắt Việt Nam', '2025-11-20 08:00:00', '2025-11-20 20:00:00', 3, 2, 400, 500000, NULL),

-- Hà Nội - Hải Phòng
('SE26', 'Đường Sắt Việt Nam', '2025-11-15 06:30:00', '2025-11-15 09:00:00', 1, 4, 300, 150000, NULL),
('SE27', 'Đường Sắt Việt Nam', '2025-11-17 06:30:00', '2025-11-17 09:00:00', 1, 4, 300, 150000, NULL),
('SE28', 'Đường Sắt Việt Nam', '2025-11-20 06:30:00', '2025-11-20 09:00:00', 1, 4, 300, 150000, NULL),

-- Hà Nội - Vinh
('SE29', 'Đường Sắt Việt Nam', '2025-11-16 05:00:00', '2025-11-16 11:00:00', 1, 6, 350, 300000, NULL),
('SE30', 'Đường Sắt Việt Nam', '2025-11-23 05:00:00', '2025-11-23 11:00:00', 1, 6, 350, 300000, NULL);

-- ============================================
-- INSERT DỮ LIỆU CHO BẢNG SEATS
-- Sử dụng stored procedure với điều kiện giá
-- ============================================

DELIMITER $$
CREATE PROCEDURE InsertTrainSeats()
BEGIN
    DECLARE f INT DEFAULT 1;
    DECLARE c INT DEFAULT 1;
    DECLARE row_letter CHAR(1);
    DECLARE col_number INT;
    DECLARE class_type VARCHAR(50);
    DECLARE seat_price FLOAT;
    DECLARE max_flights INT;
    
    -- Lấy số lượng flights hiện có
    SELECT COUNT(*) INTO max_flights FROM flights;
    
    WHILE f <= max_flights DO
        SET c = 1;
        WHILE c <= 10 DO
            -- Xác định loại toa và giá
            IF c <= 2 THEN
                SET class_type = 'Giường nằm';
                SET seat_price = 1200000;
            ELSE
                SET class_type = 'Ghế ngồi';
                SET seat_price = 900000;
            END IF;
            
            SET col_number = 1;
            WHILE col_number <= 10 DO
                SET row_letter = 'A';
                WHILE row_letter <= 'D' DO
                    INSERT INTO seats (
                        availability_status,
                        class_type,
                        coach_number,
                        seat_number,
                        flight_id,
                        hold_expiration,
                        price
                    )
                    VALUES (
                        'AVAILABLE',
                        class_type,
                        c,
                        CONCAT(row_letter, col_number),
                        f,
                        NULL,
                        seat_price
                    );
                    SET row_letter = CHAR(ASCII(row_letter) + 1);
                END WHILE;
                SET col_number = col_number + 1;
            END WHILE;
            
            SET c = c + 1;
        END WHILE;
        SET f = f + 1;
    END WHILE;
END$$
DELIMITER ;

-- Gọi stored procedure để insert seats
CALL InsertTrainSeats();

-- Xóa stored procedure sau khi sử dụng (optional)
DROP PROCEDURE IF EXISTS InsertTrainSeats;