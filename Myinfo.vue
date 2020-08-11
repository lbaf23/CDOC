<template>
    <div>
        <div class="background">
            <img :src="imgSrc" width="100%" height="100%" alt="" />
        </div>
        <van-sticky>
            <div class="up" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)" >
              <div style="padding:15px 0;">
                <van-row>
                  <van-col span="4">
                      <span style="font-size:21px;">金刚石文档</span>
                  </van-col>
                  <van-col span="12">
                      <span style="font-size:25px;color:green;"></span>
                  </van-col>
                  <van-col span="4">
                       
                  </van-col>
                  <van-col span=2>
                      <i class="el-icon-bell" style="font-size:30px;"></i>
                  </van-col>
                  <van-col span="2">
                      <el-dropdown @command="onCommand">
                        <span class="el-dropdown-link">
                          <van-image
                          round
                          width="30px"
                          height="30px"
                          fit="cover"
                          :src="userhead"/><i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item icon="el-icon-user" @click.native="toMyinfo">个人信息</el-dropdown-item>
                          <el-dropdown-item icon="el-icon-warning-outline"  @click.native="toLogin"><span style="color:red;">退出账号</span></el-dropdown-item>
                          <el-dropdown-item icon="el-icon-circle-plus-outline">螺蛳粉</el-dropdown-item>
                          
                        </el-dropdown-menu>
                        
                      </el-dropdown>
                  </van-col>
                </van-row>
              </div>
            </div>
        </van-sticky>

        <div style="margin-top:50px;">
          <van-row>
            <van-col span="6"></van-col>
            <van-col span="4">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/Home' }">主页</el-breadcrumb-item>
                <el-breadcrumb-item><a href="/">个人信息</a></el-breadcrumb-item>
              </el-breadcrumb>
            </van-col>
            <van-col span="14"></van-col>
          </van-row>
        </div>

        <br/>
    
        <div class="div1" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2)" >
            <van-row>
              <van-col span="6" style="margin-top:200px;">
                <span style="">
                    <van-image
                    round
                    width="110px"
                    height="110px"
                    fit="cover"
                    :src="userhead"/>
                    <p>个人信息</p>
                </span>
              </van-col>

              <van-col span="18" style="margin-top:200px;">
                <van-row>
                  <van-col span="2">
                  </van-col>

                  <van-col span="3">
                    <span style="">
                      <i class="el-icon-message"></i>邮箱
                    </span>
                  </van-col>
                  <van-col span="12">
                    <span style="">
                      {{useremail}}
                    </span>
                  </van-col>
                  <van-col span="5">
                    <span style="">
                      
                    </span>
                  </van-col>

                  <van-col span="2">
                  </van-col>
                </van-row>

                <van-row style="margin-top:16px;">
                  <van-col span="2">
                  </van-col>
                  <van-col span="3">
                    <span style="">
                      <i class="el-icon-lock"></i>密码
                    </span>
                  </van-col>
                  <van-col span="12">
                    <span style="">
                      {{userpassword}}
                    </span>
                  </van-col>
                  <van-col span="5">
                    <span style="">
                      <el-link type="primary">修改</el-link>
                    </span>
                  </van-col>
                  <van-col span="2">
                  </van-col>
                </van-row>

                <van-row style="margin-top:16px;">
                  <van-col span="2">
                  </van-col>
                  <van-col span="3">
                    <span style="">
                      <i class="el-icon-user"></i>用户名
                    </span>
                  </van-col>
                  <van-col span="12">
                    <span style="">
                      {{username}}
                    </span>
                  </van-col>
                  <van-col span="5">
                    <span style="">
                      <el-link type="primary">修改</el-link>
                    </span>
                  </van-col>
                  <van-col span="2">
                  </van-col>
                </van-row>

              </van-col>
            </van-row>
        </div>

    </div>
</template>

<script>
    export default{
        name: "Home",
        data(){
            return{
                myemail:localStorage.getItem('myemail'),
                imgSrc:require('../assets/loginback.jpg'),
                userhead:"https://img.yzcdn.cn/vant/cat.jpeg",
                useremail:'1322496098@qq.com',
                username:'youabcd',
                userpassword:'*****************',
            }
        },
        methods:{
            onCommand(command){
            },
            toMyinfo(){
                this.$router.push({
                    path:'/Myinfo',
                });
            },
            toLogin(){
                localStorage.setItem('myemail','');
                this.$router.push({
                    path:'/',
                });
            },
        },
    }
</script>

<style>
.up{
  background:white;
  width:100%;
  height:55px;
  
}
.background{
    width:100%;  
    height:100%;  /**宽高100%是为了图片铺满屏幕 */
    z-index:-1;
    position: fixed;
}
.div1{
  background:white;
  width:800px;
  height:550px;
  margin:0 auto;
}
.el-divider{
  height:400px;

}
</style>