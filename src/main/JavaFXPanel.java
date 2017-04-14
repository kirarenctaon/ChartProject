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
	
	//차트보여주기 이전에 DB구축해야함
	public void getGenderData(){//성별분석
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		StringBuffer sql=new StringBuffer();
		
		sql.append("select gender, count(gender), count(*) as 응시자수,");
		sql.append(" (select count(*) from score) as 합계,");
		sql.append("	count(*)/(select count(*) from score)*100 as 비율");  
		sql.append("	from score group by gender");

		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//데이터를 심기전에 이미 dataSet이 존재해야함
			dataSet=new DefaultPieDataset();
			
			while(rs.next()){
				dataSet.setValue(rs.getString("grade"), rs.getInt("비율"));			
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
	
	public void getGradeData(){//학년별분석
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();

		sql.append("select grade as 학년, count(grade) as 응시자수,");
		sql.append(" (count(grade)/(select count(grade) from score))*100 as 비율");
		sql.append("	from score group by grade");
		
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//데이터를 심기전에 이미 dataSet이 존재해야함
			dataSet=new DefaultPieDataset();
			
			while(rs.next()){
				dataSet.setValue(rs.getString("grade"), rs.getInt("비율"));			
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
		
		chart=ChartFactory.createPieChart("성적 데이터 분석", dataSet, true, true, false);
		
		//현재 차트에 설정된 폰으를 한글 폰토르 바뀌지 않으면 깨진다. 
		// System.out.println(chart.getTitle().getFont().getFontName()); Tahoma Bold체라서 깨짐
		
		Font oldTitle=chart.getTitle().getFont(); //기존폰트의 레퍼런스를 얻어와 폰트외 요소는 그대로 유지하도록함
		Font oldLegend=chart.getLegend().getItemFont(); 
		
		PiePlot plot=(PiePlot)chart.getPlot();
		plot.setLabelFont(new Font("굴림", oldTitle.getStyle(), oldTitle.getSize()));
		
		chart.getTitle().setFont(new Font("굴림", oldTitle.getStyle(), oldTitle.getSize()));
		chart.getLegend().setItemFont(new Font("굴림", oldLegend.getStyle(), oldLegend.getSize()));
		ChartPanel chartPanel=new ChartPanel(chart);
		return chartPanel;
	}

}
