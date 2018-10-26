package de.dumischbaenger.kerberos;

import java.security.Principal;

public class Role implements Principal, java.io.Serializable {
  private static final long serialVersionUID = 1L;

  private String roleName;

  public Role(String roleName) {
    if (roleName == null) {
      throw new NullPointerException("illegal null input");
    }
    this.roleName = roleName;
  }

  public String getName() {
    return roleName;
  }

  public String toString() {
    return (this.getClass().getName() + roleName);
  }

  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (this == o) {
      return true;
    }

    if (!(o instanceof Role)) {
      return false;
    }
    Role that = (Role) o;

    if (this.getName().equals(that.getName())) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return roleName.hashCode();
  }
}