package personal.ch.trainbookingsystem.repository;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import personal.ch.trainbookingsystem.entity.Users;

import java.util.List;

@Mapper
public interface TestPage {
    List<Users> findAllUsers();
}
