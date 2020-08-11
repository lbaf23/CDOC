<template>
<div class="helloworld">

    <div class="background">
      <img :src="imgSrc" width="100%" height="100%" alt="" />
    </div>

    <van-row>
      <van-col span="5">
        <span style="font-size:24px;">金刚石文档</span>
      </van-col>
      <van-col span="13">
      </van-col>
      <van-col span="6">
        <span style="color:blue;">没有账号？请</span>
        <el-button size="small" @click="toRegister"><span style="font-size:16px;">注册</span></el-button>
      </van-col>
    </van-row>

</br></br></br></br></br>



        <div class="div1" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2)" >

          <van-row>
            <van-col span="2"></van-col>
            <van-col span="20">
            <br/><br/>
            <h2 style="color: darkgrey">用户登录</h2><br/><br/>

          <van-form @submit="onSubmit">
            <van-field
              v-model="useremail"
              name="邮箱"
              type="email"
              label="邮箱"
              placeholder="请输入邮箱"
              :rules="[{ required: true, message: '请填写正确邮箱' }]"/>

            <van-field
              v-model="password"
              type="password"
              name="密码"
              label="密码"
              placeholder="请输入密码"
              :rules="[{ required: true, message: '请填写密码' }]"/>

              <van-row style="margin-top:16px;">
                <van-col span="2">
                  <el-link type="primary" style="width:200px" @click="toRegister">注册</el-link>
                </van-col>
                <van-col span="12"> </van-col>
                <van-col span="4">
                  <el-link type="primary" style="width:200px" @click="toFindPass">忘记密码？</el-link>
                </van-col>
              </van-row>

           <div style="margin: 16px;">
              <van-button block type="info" native-type="submit">立即登入</van-button>
            </div>
          </van-form>



          </van-col>

          <van-col span="2"></van-col>

          </van-row>
        </div>


  <br/><br/><br/><br/>

</div>
</template>

<script>
    import axios from 'axios';
    import baseUrl from './baseUrl'
    import {Toast} from "vant";
    export default {
      name: "Login",
      data() {
        return {
          imgSrc:require('../assets/loginback.jpg'),
          useremail:'',
          password:'',
        }
      },
      methods: {
        onSubmit() {
          console.log('submit!');
          /*this.$router.push({
              path:'/Home',
            });
          */
          localStorage.setItem('myemail',this.useremail);
          let _this=this;
          let data = new FormData();
          data.append('userId',this.useremail);
          data.append('userPassword',this.password);
          axios.post(baseUrl+'/userLogin', data)
          .then(function(response){//从后端取值
            console.log(response);
            if(response.data.success === true) {
              _this.$router.push({
                path: '/Home',
              });
            }
            else { // 登录失败 ，，，
              Toast(response.data.message);
            }
          })
        },
        toRegister(){
            this.$router.push({
                path:'/Register',
            })
        },
        toFindPass(){
            this.$router.push({
                path:'/FindPassword',
            })
        },
      }

    }
</script>

<style scoped>
.background{
    width:100%;
    height:100%;  /**宽高100%是为了图片铺满屏幕 */
    z-index:-1;
    position: fixed;
}
.front{
    z-index:1;
    position: absolute;
}
.div1{
  background:white;
  width:500px;
  height:400px;
  margin:0 auto;
}

</style>
