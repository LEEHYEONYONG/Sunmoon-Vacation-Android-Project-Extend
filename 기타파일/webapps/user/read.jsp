<%@page import="java.sql.*, org.json.simple.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 try{
 Class.forName("com.mysql.jdbc.Driver");
 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
 String id=request.getParameter("id");
 String sql="select * from userinfo where id=?";
 PreparedStatement ps=con.prepareStatement(sql);
 ps.setString(1, id);
 ResultSet rs=ps.executeQuery();
 JSONObject object=new JSONObject();
 if(rs.next()){
 object.put("id", rs.getString("id"));
 object.put("password", rs.getString("password"));
 object.put("name", rs.getString("name"));
 }
 out.print(object.toJSONString());
 }catch(Exception e){
 response.setStatus(400);
 }
%>
