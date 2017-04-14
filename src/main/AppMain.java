package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.DBManager;

public class AppMain extends JFrame implements ActionListener{
	JPanel p_center, p_east, p_south;
	JTable table;
	JScrollPane scroll;
	JButton bt_save, bt_graph;
	DBManager manager= DBManager.getInstance();
	Connection con;
	MyModel model;
	PieChartPanel pie;
	JFileChooser chooser;
	FileOutputStream fos;
	
	public AppMain() {
		con=manager.getConnection();
		p_center=new JPanel();
		p_east=new JPanel();
		p_south=new JPanel();
		table=new JTable(model=new MyModel(con));
		scroll=new JScrollPane(table);
		bt_save=new JButton("������ ����");
		bt_graph=new JButton("�׷��� ����");
		pie=new PieChartPanel(con); 
		chooser=new JFileChooser();
		
		//����
		p_east.setPreferredSize(new Dimension(450, 450));

		p_center.setLayout(new BorderLayout());	
		p_center.add(scroll);
		
		p_south.add(bt_save);
		p_south.add(bt_graph);
			
		add(p_center);
		add(p_east, BorderLayout.EAST);
		add(p_south, BorderLayout.SOUTH);
		
		//������� ������ ���� : ������ â�� ���� �� ��� ������ �������� ����
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				manager.disConnect(con);
				System.exit(0);
			}
		});
		bt_save.addActionListener(this);
		bt_graph.addActionListener(this);
		
		setSize(900, 500);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);   addWindowListener���� �ذ��ϹǷ� �����ʿ����. 
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_save){
			save();
		}else if(obj==bt_graph){
			graph();
		}
	}

	//������ �����ϱ�
	public void save(){
		HSSFWorkbook workbook = new HSSFWorkbook(); //���� ���� �ϳ��� �����Ǵ� ����
		HSSFSheet sheet=workbook.createSheet("������������"); //���� ��Ʈ �ϳ��� �����Ǵ� ����
		
		for(int i=0;i<table.getRowCount();i++){//������ row�� ����, HSSFRow
			HSSFRow row=sheet.createRow(i);
			
			for(int a=0;a<table.getColumnCount();a++){//������ col����, HSSFCell
				//System.out.print(table.getValueAt(i, a)+",");
				HSSFCell cell=row.createCell(a);
				cell.setCellValue((String)table.getValueAt(i, a)); //���������� �޸𸮻� ������ �ø�
			}
			//System.out.println("");
		}
		
		int result = chooser.showSaveDialog(this);
		if(result==chooser.APPROVE_OPTION){
			//����ڴ� Ȯ���� .xls�� �ٿ����Ѵ�. 
			File file=chooser.getSelectedFile();
			try {
				fos = new FileOutputStream(file);
				workbook.write(fos);
				JOptionPane.showMessageDialog(this, "������ ���� �Ϸ�");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		
		}
	}
	
	//�׷��� ����
	public void graph(){ //���ϴ� ������ ���̰� �����ڸ� �̿���ϰ� �Լ� �̿�
		p_east.add(pie.showChart());
		p_east.updateUI();
	}
	
	public static void main(String[] args) {
		new AppMain();
	}

}
