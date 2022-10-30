package com.wl.config;




import java.util.ArrayList;
import java.util.List;

@Deprecated
//@Configuration
//@EnableSwagger2WebMvc
public class Knife4jConfig {
//
//    @Bean
//    public Docket adminApiConfig() {
//        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        tokenPar.name("token")
//                .description("用户token")
//                .defaultValue("")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//        pars.add(tokenPar.build());
//        //添加head参数end
//
//        Docket adminApi = new Docket(DocumentationType.SWAGGER_2)
//                .groupName("adminApi")
//                .apiInfo(adminApiInfo())
//                .select()
//                //只显示admin路径下的页面
//                .apis(RequestHandlerSelectors.basePackage("com.wl"))
//                .paths(PathSelectors.regex("/.*"))
//                .build()
//                .globalOperationParameters(pars);
//        return adminApi;
//    }
//
//    private ApiInfo adminApiInfo() {
//
//        return new ApiInfoBuilder()
//                .title("后台管理系统-API文档")
//                .description("本文档描述了后台管理系统接口定义")
//                .version("1.0")
//                .contact(new Contact("cheems", "http://wang.com", "wang@qq.com"))
//                .build();
//    }
}
