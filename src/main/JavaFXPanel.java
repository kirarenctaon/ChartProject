package main;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class JavaFXPanel{
	JFreeChart chart;
	DefaultPieDataset dataSet;
	Connection con;
	
	public JavaFXPanel(Connection con) {
		this.con=con;
	}
	
	//��Ʈ�����ֱ� ������ DB�����ؾ���
	public void getGenderData(){//�����м�
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		StringBuffer sql=new StringBuffer();
		
		sql.append("select gender, count(gender), count(*) as �����ڼ�,");
		sql.append(" (select count(*) from score) as �հ�,");
		sql.append("	count(*)/(select count(*) from score)*100 as ����");  
		sql.append("	from score group by gender");

		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//�����͸� �ɱ����� �̹� dataSet�� �����ؾ���
			dataSet=new DefaultPieDataset();
			
			while(rs.next()){
				dataSet.setValue(rs.getString("grade"), rs.getInt("����"));			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}           
	
	public void getGradeData(){//�г⺰�м�
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();

		sql.append("select grade as �г�, count(grade) as �����ڼ�,");
		sql.append(" (count(grade)/(select count(grade) from score))*100 as ����");
		sql.append("	from score group by grade");
		
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//�����͸� �ɱ����� �̹� dataSet�� �����ؾ���
			dataSet=new DefaultPieDataset();
			
			while(rs.next()){
				dataSet.setValue(rs.getString("grade"), rs.getInt("����"));			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}      
	
	public ChartPanel showChart(){
		//getGenderData();
		getGradeData();
		
		chart=ChartFactory.createPieChart("���� ������ �м�", dataSet, true, true, false);
		
		//���� ��Ʈ�� ������ ������ �ѱ� ���丣 �ٲ��� ������ ������. 
		// System.out.println(chart.getTitle().getFont().getFontName()); Tahoma Boldü�� ����
		
		Font oldTitle=chart.getTitle().getFont(); //������Ʈ�� ���۷����� ���� ��Ʈ�� ��Ҵ� �״�� �����ϵ�����
		Font oldLegend=chart.getLegend().getItemFont(); 
		
		PiePlot plot=(PiePlot)chart.getPlot();
		plot.setLabelFont(new Font("����", oldTitle.getStyle(), oldTitle.getSize()));
		
		chart.getTitle().setFont(new Font("����", oldTitle.getStyle(), oldTitle.getSize()));
		chart.getLegend().setItemFont(new Font("����", oldLegend.getStyle(), oldLegend.getSize()));
		ChartPanel chartPanel=new ChartPanel(chart);
		return chartPanel;
	}

}
