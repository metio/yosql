package com.example.app;

import com.example.persistence.AdminRepository;
import com.example.persistence.CompanyRepository;
import com.example.persistence.UserRepository;

public class ExampleApp {

    public static final void main(final String[] arguments) {
        final AdminRepository adminRepository = new AdminRepository();
        adminRepository.adminUser();
        adminRepository.batchAdminUser();
        adminRepository.streamEagerAdminUser();
        final CompanyRepository companyRepository = new CompanyRepository();
        companyRepository.queryAllCompanies();
        companyRepository.batchQueryAllCompanies();
        companyRepository.streamEagerQueryAllCompanies();
        final UserRepository userRepository = new UserRepository();
        userRepository.adminUser();
        userRepository.batchAdminUser();
        userRepository.streamEagerAdminUser();
        userRepository.queryAllUsers();
        userRepository.batchQueryAllUsers();
        userRepository.streamEagerQueryAllUsers();
        userRepository.querySingleUser(1);
        userRepository.batchQuerySingleUser(2);
        userRepository.streamEagerQuerySingleUser(3);
        userRepository.queryWithoutFrontMatter();
        userRepository.batchQueryWithoutFrontMatter();
        userRepository.streamEagerQueryWithoutFrontMatter();
    }

}
