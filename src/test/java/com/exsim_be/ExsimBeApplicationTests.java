package com.exsim_be;

import com.exsim_be.dao.FileBodyDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExsimBeApplicationTests {

	@Autowired
	FileBodyDao fileBodyDao;

	@Test
	void contextLoads() {
	}


}
