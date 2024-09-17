package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.common.ConflictException;
import org.shopping.common.RecordNotfoundException;
import org.shopping.entity.Account;
import org.shopping.form.AccountChangePasswordForm;
import org.shopping.repository.AccountRepository;
import org.shopping.utils.ConvertUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account findByUserName(String username) {
        Account account = accountRepository.findByUsername(username);
        return account;
    }

    public void updateProfile(Account account) {
        Optional<Account> findAccount = accountRepository.findById(account.getId());
        if (findAccount.isPresent()){
            Account accountFromDb = findAccount.get();
            accountFromDb.setUsername(account.getUsername());
            accountFromDb.setAddress(account.getAddress());
            accountFromDb.setDescription(account.getDescription());
            accountFromDb.setImage(account.getImage());
            accountFromDb.setUpdatedAt(new Date());
            accountRepository.save(accountFromDb);
        }
    }

    public Optional<Account> selectById(Integer id) {
        Optional<Account> account = accountRepository.findById(id);
        return account;
    }

    // Phương thức để xác định loại ảnh từ dữ liệu ảnh
    public String getImageContentType(byte[] imageData) {
        // Sử dụng một cách kiểm tra đơn giản dựa trên byte array
        if (imageData.length > 4) {
            // Kiểm tra các magic numbers cơ bản của file ảnh
            if ((imageData[0] & 0xFF) == 0xFF && (imageData[1] & 0xFF) == 0xD8) {
                return "image/jpeg"; // JPEG
            } else if ((imageData[0] & 0xFF) == 0x89 && new String(imageData, 1, 3).equals("PNG")) {
                return "image/png"; // PNG
            } else if (new String(imageData, 0, 3).equals("GIF")) {
                return "image/gif"; // GIF
            }
        }
        return null;
    }

    public void updatePassword(AccountChangePasswordForm accountChangePasswordForm) {
        Optional<Account> DbAccount = accountRepository.findById(accountChangePasswordForm.getId());
        if (DbAccount.isEmpty()){
            throw new UsernameNotFoundException("Don't exist account");
        }

        Account existingAccount = DbAccount.get();
        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(accountChangePasswordForm.getCurrentPassword(), existingAccount.getPassword())) {
            throw new ConflictException("Current password is incorrect");
        }

        // Mã hóa mật khẩu mới trước khi lưu
        String newHashedPassword = passwordEncoder.encode(accountChangePasswordForm.getNewPassword());
        existingAccount.setUpdatedAt(new Date());
        existingAccount.setPassword(newHashedPassword);
        accountRepository.save(existingAccount);
    }
}
