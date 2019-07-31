package com.lzq.win;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.lzq.Program;
import com.lzq.entity.User;
import com.lzq.util.HttpClientUtil;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Grabbing extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextArea textArea;
	private Timer timer;
	private JLabel label_1;

	/**
	 * Launch the application.
	 */
	public static void open() {
		try {
			Grabbing dialog = new Grabbing();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startGrab() {
		if (timer != null) {
			return;
		}
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				chooseCourse(getTeachId());
			}
		}, 1000, 1000);
	}
	protected String getTeachId() {
		// TODO Auto-generated method stub
		String teachId = null;
		DefaultTableModel model = BrowserWin.getModel();
		for(int i = 0;i < model.getRowCount();i++) {
			String bianhao = (String) model.getValueAt(i, 0);
			if (bianhao.equals(textField.getText())) {
				teachId = (String) model.getValueAt(i, 1);
				label_1.setText("课程名称：" + (String) model.getValueAt(i, 3));
			}
		}
		return teachId;
	}

	private void stopGrab() {
		if (timer != null) {
			timer.cancel();
			System.out.println(timer);
			timer = null;
		}
	}

	/**
	 * Create the dialog.
	 */
	public Grabbing() {
		setBounds(100, 100, 518, 386);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("课程编号：");
		label.setBounds(63, 33, 66, 15);
		contentPanel.add(label);
		
		textField = new JTextField();
		textField.setBounds(139, 30, 308, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		label_1 = new JLabel("课程名称：");
		label_1.setBounds(63, 67, 384, 15);
		contentPanel.add(label_1);
		
		textArea = new JTextArea();
		textArea.setBounds(54, 102, 393, 202);
		contentPanel.add(textArea);
		textArea.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("开始");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startGrab();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("停止");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						stopGrab();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private String chooseCourse(String teachId) {
		
		User user = Program.getUser();
//		user.setUsername("20163594");
//		user.setJSESSIONID("D0227B745C381AA46B0F1059A8DD8924");
		Map<String, String> param = new HashMap<String,String>();
		
		param.put("teachId", teachId);
		param.put("setAction", "addStudentCourseApply");
		param.put("isBook", "1");
		param.put("tt", String.valueOf(new Date().getTime()));
		String cookies = "username=" + user.getUsername() + ";JSESSIONID=" + user.getJSESSIONID();
		String res = HttpClientUtil.doGet("http://tiedao.vatuu.com/vatuu/CourseStudentAction", param, cookies);
		textArea.setText(res + "\n" + new Date().toString());
		if (res.indexOf("选课申请成功") >= 0 || res.indexOf("冲突") >= 0) {
			stopGrab();
		}
		return null;
		
	}
}
