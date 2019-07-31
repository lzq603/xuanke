package com.lzq.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.lzq.entity.Course;
import com.lzq.mapper.CourseMapper;
import com.lzq.util.SqlSessionUtil;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Course course = new Course("1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1");
		SqlSessionFactory sessionFactory = SqlSessionUtil.getSqlSessionFactory();
		SqlSession openSession = sessionFactory.openSession();
		CourseMapper courseMapper = openSession.getMapper(CourseMapper.class);
		
		courseMapper.addCourse(course);
		openSession.commit();
	}
}
