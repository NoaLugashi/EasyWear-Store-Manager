// package com.example.demo.user;

// import java.util.List;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @Configuration
// public class UserConfig {

//     @Bean
//     CommandLineRunner commandLineRunner(
//         UserRepository repository){ // link the repository
//         return args -> {
//             BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Create BCryptPasswordEncoder instance
//             User user1 = new User(
// 				"shaki",
// 				encoder.encode("61339125"),
// 				"Admin",
//                 "Tel-Aviv"
// 		    ); 

//             User user2 = new User(
// 				"ori",
// 				encoder.encode("123456"),
// 				"Admin",
//                 "Jerusalem"
// 		    );
            
//             User user3 = new User(
// 				"test",
// 				encoder.encode("666666"),
// 				"Admin",
//                 "Haifa"
// 		    ); 
            
//             repository.saveAll( // apply sql command with jbd 
//                 List.of(user1, user2, user3)
//             );
        
//         };

//     }


    

// }
