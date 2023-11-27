package com.ecommerce.booksale.utils.constants;

public interface AuthenError {
   String NOT_MATCH_PASSWORD_ERROR = "Mật khẩu không khớp";
   String EMAIL_IN_USE = "Email này đã được sử dụng";
   String TOKEN_NOT_FOUND = "Không tìm thấy token";
   String TOKEN_CONFIRMED = "Email này đã được xác nhận";
   String EXPIRED_TOKEN = "Token đã hết hiêụ lực";
}
