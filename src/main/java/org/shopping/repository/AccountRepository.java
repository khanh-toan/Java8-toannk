	package org.shopping.repository;

	import java.util.List;
	import java.util.Optional;

	import org.shopping.entity.Account;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
	import org.springframework.stereotype.Repository;

	@Repository
	public interface AccountRepository extends BaseRepository<Account, Integer> {
		Account findByUsername(String username);

		@Query("Select a from Account a where a.username like %:name%")
		Page<Account> findByUserName(String name,Pageable pageable);

	}
