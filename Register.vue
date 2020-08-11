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
        <span style="color:blue;">已有账号？请</span>
        <el-button size="small" @click="toLogin"><span style="font-size:16px;">登入</span></el-button>
      </van-col>
    </van-row>

<br/><br/><br/><br/>



        <div class="div1" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2)" >

          <van-row>
            <van-col span="2"></van-col>
            <van-col span="20">
            <br/><br/>
            <h2 style="color: darkgrey">注册</h2><br/>

          <van-form @submit="onSubmit">
            <van-field
              v-model="useremail"
              name="邮箱"
              type="email"
              label="邮箱"
              placeholder="请输入邮箱"
              :rules="[{ required: true, message: '请填写正确邮箱' }]"/>

            <van-field
              v-model="password1"
              type="password"
              name="密码"
              label="密码"
              placeholder="请设置密码"
              :rules="[{ required: true, message: '请填写密码' }]"/>

            <van-field
              v-model="password2"
              type="password"
              name="再次输入密码"
              label="再次输入密码"
              placeholder="请输入与上一次一样的密码"
              :rules="[{ required: true, message: '请填写密码' }]"/>

            <van-field
             :rules="[{ required: true, message: '请填写验证码' }]"
             v-model="idcode"
             center
             label="邮箱验证码"
             placeholder="请输入邮箱验证码">
             <template #button>
               <van-button size="small" type="primary" @click="submitIdCode()" :disabled="disabled">{{timeContent}}</van-button>
             </template>
             </van-field>

            <div style="margin: 20px;">
              <van-button block type="info" native-type="submit">立即注册</van-button>
            </div>
          </van-form>

          <p>
          <span style="color:darkgrey;font-size:12px;">点击[注册]表示以阅读并同意金刚石文档</span><span style="font-size:12px;">服务条款</span>
          </p>

          </van-col>

          <van-col span="2"></van-col>

          </van-row>
        </div>


  <br/><br/><br/><br/>

</div>
</template>

<script>
    import axios from 'axios';
    import baseUrl from "./baseUrl";
    import Toast from "vant/lib/toast";
    export default {
      name: "Register",
      data() {
        return {
          imgSrc:require('../assets/loginback.jpg'),
          useremail:'',
          password1:'',
          password2:'',
          idcode:'',
          subidcode: '111111',
          disabled:false,
          timeContent: '发送验证码',
        }
      },
      methods: {
        onSubmit() {
          console.log('submit!');
          let _this=this;
          let data = new FormData();
          data.append('userId',this.useremail);
          data.append('password1',this.password1);
          data.append('password2',this.password2);
          data.append('idcode',this.idcode);
          data.append('subidcode',this.subidcode);
          axios.post(baseUrl+'/userRegister',data)
          .then(function(response){//从后端取值
            console.log(response);
            if(response.data.success === true){ // 注册成功
              Toast(response.data.message);
              _this.$router.push({
                path:'/',
              });
            }
            else{
              Toast(response.data.message);
            }

          })
        },
        toLogin(){
            this.$router.push({
                path:'/',
            })
        },
        submitIdCode() {
          var _this = this;
          if(this.timeContent=='发送验证码'){
            let time=59;
            let timer=setInterval(()=>{
              if(time>0){
                  this.timeContent=time+'s';
                  this.disabled=true;
                  time--;
              }
              else{
                  window.clearInterval(timer);
                  this.disabled=false;
                  this.timeContent='发送验证码';
              }
            },1000);
          }
          let data = new FormData();
          data.append('userId',this.useremail);
          axios.post(baseUrl+'/sendEmail',data)
          .then(function (response){
            Toast(response.data.message);
            console.log(response);
            _this.subidcode = response.data.result;
          })
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
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
  height:500px;
  margin:0 auto;
}

</style>
