<%@page import="java.sql.*"%>
<%
 request.setCharacterEncoding("UTF-8");
 try{
 Class.forName("com.mysql.jdbc.Driver");
 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
 String id=request.getParameter("id");
 String password=request.getParameter("password");
 String name=request.getParameter("name");
 System.out.println("id=" + id);
 String sql="update userinfo set password=?, name=? where id=?";
 PreparedStatement ps=con.prepareStatement(sql);
 ps.setString(1, password);
 ps.setString(2, name);
 ps.setString(3, id);
 ps.execute();
 }catch(Exception e){
 response.setStatus(400);
 out.print(e.toString());
 }
%> 
