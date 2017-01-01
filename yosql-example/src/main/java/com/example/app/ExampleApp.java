package com.example.app;

import com.example.persistence.AdminRepository;
import com.example.persistence.CompanyRepository;
import com.example.persistence.UserRepository;

public class ExampleApp {

    public static final void main(final String[] arguments) {
        final AdminRepository adminRepository = new AdminRepository();
        adminRepository.adminUser(arguments);
        adminRepository.batchAdminUser(arguments);
        adminRepository.streamAdminUser(arguments);
        final CompanyRepository companyRepository = new CompanyRepository();
        companyRepository.queryAllCompanies(arguments);
        companyRepository.batchQueryAllCompanies(arguments);
        companyRepository.streamQueryAllCompanies(arguments);
        final UserRepository userRepository = new UserRepository();
        userRepository.adminUser(arguments);
        userRepository.batchAdminUser(arguments);
        userRepository.streamAdminUser(arguments);
        userRepository.queryAllUsers(arguments);
        userRepository.batchQueryAllUsers(arguments);
        userRepository.streamQueryAllUsers(arguments);
        userRepository.querySingleUser(arguments);
        userRepository.batchQuerySingleUser(arguments);
        userRepository.streamQuerySingleUser(arguments);
        userRepository.queryWithoutFrontMatter(arguments);
        userRepository.batchQueryWithoutFrontMatter(arguments);
        userRepository.streamQueryWithoutFrontMatter(arguments);
    }

}
