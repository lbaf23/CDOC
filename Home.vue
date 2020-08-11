<template>
    <div>
        <!--<div class="background">
            <img :src="imgSrc" width="100%" height="100%" alt="" />
        </div>-->
        <van-sticky>
            <div class="up" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)" >
              <div style="padding:15px 0;">
                <van-row>
                  <van-col span="2">
                      <span style="font-size:21px;">金刚石文档</span>
                  </van-col>
                  <van-col span="16">
                      <span style="font-size:25px;color:green;"></span>
                  </van-col>
                  <van-col span="4">
                       <el-input placeholder="搜索文件"
                       size="small"
                       prefix-icon="el-icon-search"
                       v-model="input2">
                       </el-input>
                  </van-col>
                  <van-col span=1>
                      <i class="el-icon-bell" style="font-size:30px;"></i>
                  </van-col>
                  <van-col span="1">
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
                          <el-dropdown-item icon="el-icon-circle-plus-outline">帮助</el-dropdown-item>
                          
                        </el-dropdown-menu>
                        
                      </el-dropdown>
                  </van-col>
                </van-row>
              </div>
            </div>
        </van-sticky>


      <div>
        <van-row  style="margin-top:15px;">
          <van-col span="3">
            <van-sidebar v-model="activeKey" @change="onChange" style="width:100%;">
              <van-sidebar-item title="工作台" />
              <van-sidebar-item title="收件箱" />
              <van-sidebar-item title="我的桌面" />
              <van-cell is-link style="width:100%;" @click="openteam">
                  <van-sidebar-item title="团队空间" @click="openteam" />
              </van-cell>
              <van-sidebar-item title="回收站" />
              <van-sidebar-item title="帮助中心" />
            </van-sidebar>
          </van-col>

          <van-col span="17">
            <div v-if="index1">
              <van-tabs v-model="active" style="width:80%;">
                <van-tab title="最近使用">内容 1</van-tab>
                <van-tab title="我创建的">内容 2</van-tab>
                <van-tab title="我的收藏">内容 3</van-tab>
                <van-tab title="工作动态">内容 4</van-tab>
              </van-tabs>
            </div>

            <div v-if="index2">
                收件箱
            </div>

            <div v-if="index3">
                我的桌面
            </div>

            <div v-if="index4">
                团队空间
            </div>

            <div v-if="index5">
                回收站
            </div>

            <div v-if="index6">
                帮助中心
            </div>

          </van-col>

          <van-col span="3">
              <van-button type="primary" block style="margin-top:66px;">新建</van-button>
              <van-button type="info" block style="margin-top:16px;">模板库</van-button>
              <van-button type="info" block style="margin-top:16px;">导入</van-button>
          </van-col>

          <van-col span="1">
          </van-col>
        </van-row>
      </div>

      <van-popup
      v-model="showpop"
      closeable
      position="left"
      :style="{ height: '100%',width:'50%'}">
          <van-cell v-for="(item,index) in teams" :key="item" :title="teams[index]" clickable @click="toteamwork(item)" style="margin-top:34px;">
              <template #icon>
                    <van-image
                        width="80"
                        height="80">
                    </van-image>
              </template>
              <template #default>
                  <div style="color: #FF0000">团队创建者:</div><div style="font-size: 20px; color: #FF0000">{{teamowner[index]}}</div>
              </template>
          </van-cell>
      </van-popup>

    </div>
</template>

<script>
    import {Toast} from "vant";

    export default{
        name: "Home",
        data(){
            return{
                myemail:localStorage.getItem('myemail'),
                imgSrc:require('../assets/loginback.jpg'),
                userhead:"https://img.yzcdn.cn/vant/cat.jpeg",
                index1:true,
                index2:false,
                index3:false,
                index4:false,
                index5:false,
                index6:false,
                activeKey: 0,
                active: 0,
                showpop:false,
                teams:['a','b','c','d','a','b','c','d','a','b','c','d'],
                teamowner:['she','you','he','she','you','he','she','you','he'],
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
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            openteam(){
                this.showpop=true;
            },
            onChange(index) {
                if(index==0){
                    this.index1=true;
                    this.index2=false;
                    this.index3=false;
                    this.index4=false;
                    this.index5=false;
                    this.index6=false;
                }
                else if(index==1){
                    this.index1=false;
                    this.index2=true;
                    this.index3=false;
                    this.index4=false;
                    this.index5=false;
                    this.index6=false;
                }
                else if(index==2){
                    this.index1=false;
                    this.index2=false;
                    this.index3=true;
                    this.index4=false;
                    this.index5=false;
                    this.index6=false;
                }
                else if(index==3){
                    this.index1=false;
                    this.index2=false;
                    this.index3=false;
                    this.index4=true;
                    this.index5=false;
                    this.index6=false;
                    this.show=true;
                }
                else if(index==4){
                    this.index1=false;
                    this.index2=false;
                    this.index3=false;
                    this.index4=false;
                    this.index5=true;
                    this.index6=false;
                }
                else if(index==5){
                    this.index1=false;
                    this.index2=false;
                    this.index3=false;
                    this.index4=false;
                    this.index5=false;
                    this.index6=true;
                }
            },
        },
    }
</script>

<style>
.up{
  background:white;
  width:1535px;
  height:55px;
  
}
.background{
    width:100%;  
    height:100%;  /**宽高100%是为了图片铺满屏幕 */
    z-index:-1;
    position: fixed;
}
.leftdiv{
	width:300px;
	height:800px;
}
</style>