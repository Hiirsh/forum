package telran.java2022.account.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EndpointChecker {
  String method;
  String servletPath;

  public boolean isPermitAllEndpointChecker() {
    return ("POST".equalsIgnoreCase(method) && servletPath.matches("/account/register/?")) ||
        (("POST".equalsIgnoreCase(method) || "GET".equalsIgnoreCase(method)) && servletPath.matches("/forum/posts/?"));
  }

  public boolean isAuthentecatedEndpointCheck() {
    return !(("POST".equalsIgnoreCase(method) && servletPath.matches("/account/register/?")) ||
        "GET".equalsIgnoreCase(method) ||
        ("POST".equalsIgnoreCase(method) && servletPath.matches("/forum/posts/(tags|period)/?")));
  }

  public boolean isAdministratorEndpointCheck() {
    return servletPath.matches("/account/user/\\w+/role/\\w+/?") ||
        ("DELETE".equalsIgnoreCase(method) && servletPath.matches("/account/user/\\w+/?F"));
  }

  public boolean isOwnerEndpointCheck() {
    return (method.toUpperCase().matches("(DELETE|PUT)") && (servletPath.matches("/(account/user|forum/post)/\\w+/?")));
  }

}
