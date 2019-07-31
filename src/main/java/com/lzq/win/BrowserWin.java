package com.lzq.win;
import java.awt.EventQueue;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;

import com.lzq.GetCourse;
import com.lzq.Program;
import com.lzq.entity.Course;
import com.lzq.entity.User;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Cookie;
import com.teamdev.jxbrowser.chromium.CookieStorage;
import com.teamdev.jxbrowser.chromium.bb;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class BrowserWin {

	private JFrame frmv;
	private JTable table;
	private static JLabel labelState = new JLabel("等待登录...");
	private static DefaultTableModel model = new DefaultTableModel(new String[] {
			"\u7F16\u53F7", "teachId", "\u8BFE\u7A0B\u4EE3\u7801", "\u8BFE\u7A0B\u540D\u79F0", "\u73ED\u53F7", "\u5B66\u5206", "\u6027\u8D28", "\u9662\u7CFB", "\u6559\u5E08", "\u804C\u79F0", "\u65F6\u95F4\u5730\u70B9", "\u4F18\u9009", "\u72B6\u6001", "\u6821\u533A", "\u9650\u5236", "\u64CD\u4F5C"
		}, 0);
	/**
	 * @wbp.nonvisual location=111,167
	 */
	
	private final Browser browser = new Browser();
	
	/**
	 * Launch the application.
	 */
	static {
        try {

            Field e = bb.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = bb.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
	public static void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BrowserWin window = new BrowserWin();
					window.frmv.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BrowserWin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmv = new JFrame();
		frmv.setTitle("铁大选课助手V1.0");
		frmv.setBounds(0, 0, 1366, 768);
		frmv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmv.getContentPane().setLayout(null);
		frmv.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		JPanel panelTools = new JPanel();
		panelTools.setBounds(0, 0, 1350, 300);
		frmv.getContentPane().add(panelTools);
		GridBagLayout gbl_panelTools = new GridBagLayout();
		gbl_panelTools.columnWidths = new int[]{156, 99, 36, 0};
		gbl_panelTools.rowHeights = new int[]{46, 0, 143, 0};
		gbl_panelTools.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panelTools.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		panelTools.setLayout(null);
		
		JLabel label = new JLabel("学号：暂无");
		label.setBounds(46, 32, 200, 15);
		panelTools.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(312, 0, 1038, 300);
		panelTools.add(scrollPane);
		
		table = new JTable();
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);;
		table.getTableHeader().setDefaultRenderer(cr);		//设置表头对齐方式
		table.setModel(model);					//设置数据
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);				//设置排序
		
		
		scrollPane.setViewportView(table);
		//查询所有可选课程
		JButton button = new JButton("查询可选课程");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Program.delAllFile("D:\\getCourse");
				while(model.getRowCount() > 0) {
					model.removeRow(0);
				}
				GetCourse.start();
			}
		});
		button.setBounds(58, 179, 130, 23);
		panelTools.add(button);
		
		/***获取登录后的Cookies***/
		JButton button_1 = new JButton("获取登录信息");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CookieStorage cookieStorage = browser.getCookieStorage();
				User user = new User();
				List<Cookie> cookies = cookieStorage.getAllCookies();
				for(Cookie cookie:cookies) {
					System.out.println(cookie);
//					System.out.println("'" + cookie.getName() + "'");
//					System.out.println("'" + cookie.getValue() + "'");
//					System.out.println("'" + cookie.getDomain() + "'");
					if (cookie.getName().equals("JSESSIONID") && cookie.getDomain().equals("tiedao.vatuu.com")) {
						user.setJSESSIONID(cookie.getValue());
					}
					if (cookie.getName().equals("username") && cookie.getDomain().equals("tiedao.vatuu.com")) {
						user.setUsername(cookie.getValue());
					}
				}
				Program.setUser(user);
				System.out.println(user.getJSESSIONID());
				label.setText("学号：" + user.getUsername());
				if (user.getUsername() == null) {
					setState("请先在下方浏览器中登录");
				}else {
					button.setEnabled(true);
					setState("登录成功");
				}
			}
		});
		button_1.setBounds(58, 132, 130, 23);
		panelTools.add(button_1);
		
		labelState.setBounds(62, 275, 200, 15);
		panelTools.add(labelState);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 302, 21);
		panelTools.add(menuBar);
		
		JMenu menu = new JMenu("工具");
		menuBar.add(menu);
		
		JMenu menu2 = new JMenu("帮助(使用必读)");
		menuBar.add(menu2);
		
		JMenuItem menuItem = new JMenuItem("自动选课");
		menuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Grabbing.open();
			}
		});
		menu.add(menuItem);
		
		JMenuItem menuItem2 = new JMenuItem("使用说明");
		menuItem2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Process process = Runtime.getRuntime().exec("explorer http://www.tunan.work:8090/archives/xuanke");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menu.add(menuItem);
		menu2.add(menuItem2);
		
		JLabel label_1 = new JLabel("1.下方浏览器中登录");
		label_1.setBounds(46, 96, 127, 15);
		panelTools.add(label_1);
		
		JLabel label_2 = new JLabel("2.");
		label_2.setBounds(46, 136, 54, 15);
		panelTools.add(label_2);
		
		JLabel label_3 = new JLabel("3.");
		label_3.setBounds(46, 183, 54, 15);
		panelTools.add(label_3);
		
		JLabel label_4 = new JLabel("状态：");
		label_4.setBounds(26, 275, 54, 15);
		panelTools.add(label_4);
		
		BrowserView view = new BrowserView(browser);
		view.setBounds(0, 300, 1350, 429);
        frmv.add(view);
        browser.getCookieStorage().deleteAll();
        browser.loadURL("http://tiedao.vatuu.com/service/login.html");
	}
	
	/***添加课程***/
	public static void addCourse(Course course) {
		model.addRow(new String[] {
			course.getBianhao(),
			course.getTeachId(),
			course.getDaima(),
			course.getMingcheng(),
			course.getBanhao(),
			course.getXuefen(),
			course.getXingzhi(),
			course.getYuanxi(),
			course.getJiaoshi(),
			course.getZhicheng(),
			course.getShijiandidian(),
			course.getYouxuan(),
			course.getZhuangtai(),
			course.getXiaoqu(),
			course.getXianzhi(),
			course.getCaozuo()});
		setState("已为你查到" + model.getRowCount() + "条数据");
	}
	//设置状态
	public static void setState(String string) {
		labelState.setText(string);
	}
	
	public static DefaultTableModel getModel() {
		return model;
	}
}
