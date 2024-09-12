package org.shopping.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shopping.entity.Account;
import org.shopping.model.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Account findAccount(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Account.class, username);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAccount(String oldUsername, String newUsername) {
        Session session = sessionFactory.getCurrentSession();

        // Tìm tài khoản dựa trên username cũ
        Account account = this.findAccount(oldUsername);

        if (account != null) {
            // Nếu tìm thấy tài khoản, cập nhật thông tin
            account.setUserName(newUsername);
            session.merge(account); // Sử dụng update để lưu thay đổi
            session.flush();         // Đảm bảo các thay đổi được đẩy vào DB
        } else {
            // Nếu tài khoản không tồn tại, có thể ném ra ngoại lệ hoặc xử lý logic khác
            throw new RuntimeException("Tài khoản không tồn tại: " + oldUsername);
        }
    }
}
