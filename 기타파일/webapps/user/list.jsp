<%@page import="java.sql.*, org.json.simple.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 try{
 Class.forName("com.mysql.jdbc.Driver");
 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
 String sql="select * from userinfo";
 PreparedStatement ps=con.prepareStatement(sql);
 ResultSet rs=ps.executeQuery();
 JSONArray array=new JSONArray();
 while(rs.next()){
 JSONObject object=new JSONObject();
 object.put("id", rs.getString("id"));
 object.put("password", rs.getString("password"));
 object.put("name", rs.getString("name"));

 array.add(object);
 }
 out.print(array.toJSONString());
 }catch(Exception e){
 response.setStatus(400);
 out.print(e.toString());
 }
%>