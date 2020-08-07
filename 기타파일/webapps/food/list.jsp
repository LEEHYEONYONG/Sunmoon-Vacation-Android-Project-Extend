<%@page import="java.sql.*, org.json.simple.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 try{
    Class.forName("com.mysql.jdbc.Driver");
    Connection con=DriverManager.getConnection(
       "jdbc:mysql://localhost:3306/webdb?serverTimezone=Asia/Seoul", "root", "1234");
    String sql="select * from foodinfo";
    PreparedStatement ps=con.prepareStatement(sql);
    ResultSet rs=ps.executeQuery();
    JSONArray array=new JSONArray();
    while(rs.next()){
       JSONObject object=new JSONObject();
       object.put("seq", rs.getInt("seq"));
       object.put("name", rs.getString("name"));
       object.put("tel", rs.getString("tel"));
       object.put("address", rs.getString("address"));
       object.put("latitude", rs.getString("latitude"));
       object.put("longitude", rs.getString("longitude"));
       array.add(object);
    }
   out.print(array.toJSONString());
 }catch(Exception e){
   response.setStatus(400);
   out.print(e.toString());
 }
%>