package com.wei.mySecurity;

import java.util.List;

public interface SecureDataSource {

    List<String> getRoleDate();

    List<String> getPermissionDate();

    List<String> getPermissionByRole();
}
