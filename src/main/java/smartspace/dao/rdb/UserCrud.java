package smartspace.dao.rdb;

import org.springframework.data.repository.PagingAndSortingRepository;

import smartspace.data.UserEntity;

public interface UserCrud extends PagingAndSortingRepository<UserEntity, String> {

}