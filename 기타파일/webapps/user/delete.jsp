<%@page import="java.sql.*"%>
<%
 request.setCharacterEncoding("UTF-8");
 try{
 Class.forName("com.mysql.jdbc.Driver");
 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
 request.setCharacterEncoding("UTF-8");
 String id=request.getParameter("id");
 String sql="delete from userinfo where id=?";
 PreparedStatement ps=con.prepareStatement(sql);
 ps.setString(1, id);
 ps.execute();
 }catch(Exception e){
 response.setStatus(400);
}
%>