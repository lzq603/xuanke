package com.lzq;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.lzq.entity.Course;
import com.lzq.entity.User;
import com.lzq.mapper.CourseMapper;
import com.lzq.util.SqlSessionUtil;
import com.lzq.win.BrowserWin;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

public class GetCourse implements PageProcessor{
	
	private static Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	@Override
	public void process(Page page) {
		
		Selectable courseTable = page.getHtml().xpath("//table[@id='table3']");
		List<Course> courses = new ArrayList<Course>();
		Course course = null;
		for(int i = 2;i <= 51;i++) {
			Selectable courseRow = courseTable.xpath("//tr[" + i + "]");
			
			String caozuo = "";
			
			String cz1 = courseRow.xpath("//td[16]/span[1]/text()").toString();
			String cz2 = courseRow.xpath("//td[16]/text()").toString();
			String cz3 = courseRow.xpath("//td[16]/input/@value").toString();
			if (cz1 != null) {
				caozuo += cz1;
			}
			if (cz2 != null) {
				caozuo += " " + cz2;
			}
			if (cz3 != null) {
				caozuo += " " + cz3;
			}else {
				continue;
			}
			
			String bianhao = courseRow.xpath("//td[2]/span[2]/text()").toString();
			String teachId = courseRow.xpath("//td[2]/span[1]/text()").toString();
			String daima = courseRow.xpath("//td[3]/a/span[1]/text()").toString();
			String mingcheng = courseRow.xpath("//td[4]/a/span[1]/text()").toString();
			String banhao = courseRow.xpath("//td[5]/span[1]/text()").toString();
			String xuefen = courseRow.xpath("//td[6]/a/span[1]/text()").toString();
			String xingzhi = courseRow.xpath("//td[7]/a/span[1]/text()").toString();
			String yuanxi = courseRow.xpath("//td[8]/a/text()").toString();
			String jiaoshi = courseRow.xpath("//td[9]/a/span[1]/text()").toString();
			String zhicheng = courseRow.xpath("//td[10]/text()").toString();
			String shijiandidian = courseRow.xpath("//td[11]/text()").toString();
			String youxuan = courseRow.xpath("//td[12]/text()").toString();
			String zhuangtai = courseRow.xpath("//td[13]/text()").toString();
			String xiaoqu = courseRow.xpath("//td[14]/a/span[1]/text()").toString();
			String xianzhi = courseRow.xpath("//td[15]/a/u/text()").toString();
			
			course = new Course(bianhao, teachId, daima, mingcheng, banhao, xuefen, xingzhi, yuanxi, jiaoshi, zhicheng, shijiandidian, youxuan, zhuangtai, xiaoqu, xianzhi, caozuo);
			courses.add(course);
		}
		page.putField("coursesList", courses);
	}

	@Override
	public Site getSite() {
		User user = Program.getUser();
		site.addCookie("JSESSIONID", user.getJSESSIONID());
		site.addCookie("username", user.getUsername());
		return site;
	}
	
	public static void start() {
		
		BrowserWin.setState("该过程需要一些时间...");
		Spider spider = Spider.create(new GetCourse());
		
		for(int i = 1;i <= 75;i++) {
			spider.addUrl("http://tiedao.vatuu.com/vatuu/CourseStudentAction?setAction=studentCourseSysSchedule&viewType=&orderType=teachId&orderValue=asc&selectAction=QueryAll&key1=&key2=&key3=&key4=&courseType=all&jumpPage=" + i);
		}
		
		spider.addPipeline(new CourseJTablePipeLine());
		spider.setScheduler(new FileCacheQueueScheduler("C:\\getCourse\\"));
		spider.thread(5);
		spider.run();
	}

}


class CourseJTablePipeLine implements Pipeline{
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<Course> subCourseList = resultItems.get("coursesList");
		for(Course course:subCourseList) {
			BrowserWin.addCourse(course);
		}
	}
	
}

class CourseMySQLPipeLine implements Pipeline{
	
	private SqlSessionFactory SqlSessionFactory = SqlSessionUtil.getSqlSessionFactory();
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<Course> subCourseList = resultItems.get("coursesList");
		SqlSession openSession = SqlSessionFactory.openSession();
		CourseMapper courseMapper = openSession.getMapper(CourseMapper.class);
		for(Course course:subCourseList) {
			courseMapper.addCourse(course);
		}
		openSession.commit();
	}
	
}
