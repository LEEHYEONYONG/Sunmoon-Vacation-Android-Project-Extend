<%@page import="java.sql.*"%>
<%@page import="org.json.simple.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 try{
    Class.forName("com.mysql.jdbc.Driver");
    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
    
	String order=request.getParameter("order");
	String query=request.getParameter("query");
	String sql="select * from productinfo";
	sql += " where pname like '%" + query + "%'";
    switch(order){
		case "code":
			sql += " order by code";
			break;
		case "pname":
			sql += " order by pname";
			break;
		case "desc":
			sql += " order by price desc";
			break;
		case "asc":
			sql += " order by price asc";
			break;
	}
    
	PreparedStatement ps=con.prepareStatement(sql);
	//ps.setString(1, query);

    ResultSet rs=ps.executeQuery();
    JSONArray array=new JSONArray();
    while(rs.next()){
       JSONObject object=new JSONObject();
       object.put("code", rs.getString("code"));
       object.put("pname", rs.getString("pname"));
       object.put("price", rs.getInt("price"));
       object.put("image", rs.getString("image"));
       array.add(object);
   }
   out.print(array.toJSONString());
 }catch(Exception e){
   response.setStatus(400);
 }
%>