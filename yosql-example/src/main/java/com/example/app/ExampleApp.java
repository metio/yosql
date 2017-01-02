package com.example.app;

import com.example.persistence.AdminRepository;
import com.example.persistence.CompanyRepository;
import com.example.persistence.UserRepository;

public class ExampleApp {

    public static final void main(final String[] arguments) {
        final AdminRepository adminRepository = new AdminRepository();
        adminRepository.adminUser();
        adminRepository.batchAdminUser();
        adminRepository.streamAdminUser();
        final CompanyRepository companyRepository = new CompanyRepository();
        companyRepository.queryAllCompanies();
        companyRepository.batchQueryAllCompanies();
        companyRepository.streamQueryAllCompanies();
        final UserRepository userRepository = new UserRepository();
        userRepository.adminUser();
        userRepository.batchAdminUser();
        userRepository.streamAdminUser();
        userRepository.queryAllUsers();
        userRepository.batchQueryAllUsers();
        userRepository.streamQueryAllUsers();
        userRepository.querySingleUser();
        userRepository.batchQuerySingleUser();
        userRepository.streamQuerySingleUser();
        userRepository.queryWithoutFrontMatter();
        userRepository.batchQueryWithoutFrontMatter();
        userRepository.streamQueryWithoutFrontMatter();
    }

}
