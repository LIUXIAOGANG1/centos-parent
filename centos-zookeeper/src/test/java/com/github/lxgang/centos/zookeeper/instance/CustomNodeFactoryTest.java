package com.github.lxgang.centos.zookeeper.instance;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class CustomNodeFactoryTest {
	private static Logger logger = LoggerFactory.getLogger(CustomNodeFactoryTest.class);

	@Resource
	private CustomNodeFactory customNodeFactory;
	
	@Test
	public void test() throws InterruptedException {
		CustomNode customNode = customNodeFactory.getInstance("/testSpring");
		
		try {
			customNode.create();
			customNode.setContent("5");
			
			String content = verifyContent(customNode);
			
			logger.info("-----------------------------------------------" + content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String verifyContent(CustomNode customNode){
		String content = customNode.getContent();
		
		if(content == null){
			try {
				logger.info("延时1秒准备等待");
				TimeUnit.SECONDS.sleep(1);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return verifyContent(customNode);
		}
		
		return content;
	}
}
