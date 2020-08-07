<%@page import="java.sql.*"%>
<%@page import="org.json.simple.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 try{
    Class.forName("com.mysql.jdbc.Driver");
    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
    
	String code=request.getParameter("code");
	String sql="select * from productinfo where code=?";

	PreparedStatement ps=con.prepareStatement(sql);
	ps.setString(1, code);

    ResultSet rs=ps.executeQuery();
    //JSONArray array=new JSONArray();
    while(rs.next()){
       JSONObject object=new JSONObject();
       object.put("code", rs.getString("code"));
       object.put("pname", rs.getString("pname"));
       object.put("price", rs.getInt("price"));
       object.put("image", rs.getString("image"));
	   out.print(object.toJSONString());
       //array.add(object);
   }
 //  out.print(array.toJSONString());
 }catch(Exception e){
	out.print(e.toString());
   response.setStatus(400);
 }
%>