<%@ page import="com.classparser.api.ClassParser" %>
<%@ page import="com.classparser.reflection.impl.ReflectionParser" %><%

    ClassParser parser = new ReflectionParser();
    out.write(parser.parseClass(String.class));
%>