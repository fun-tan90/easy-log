var k=(x,r,i)=>new Promise((t,c)=>{var p=s=>{try{u(i.next(s))}catch(d){c(d)}},m=s=>{try{u(i.throw(s))}catch(d){c(d)}},u=s=>s.done?t(s.value):Promise.resolve(s.value).then(p,m);u((i=i.apply(x,r)).next())});import{u as z,a as I,b as R,L as F,_ as P}from "./LoginFormTitle.vue_vue_type_script_setup_true_lang-64884d13.js";import{S as L}from "./index-b7948cc7.js";import{C as U}from "./index-93d3fdab.js";import{d as B}from "./index.js";import{aL as w,Z as g,ah as E,a1 as _}from "./antd-685f22a1.js";import{d as N,k as b,r as V,e as T,u as e,_ as D,$,f as a,a9 as n,E as v,a1 as y,ab as G}from "./vue-50936e44.js";const M={key:0},O=N({__name:"RegisterForm",setup(x){const r=w.Item,i=g.Password,{t}=B(),{handleBackLogin:c,getLoginState:p}=z(),m=b(),u=b(!1),s=V({account:"",password:"",confirmPassword:"",mobile:"",sms:"",policy:!1}),{getFormRules:d}=I(s),{validForm:h}=R(m),C=T(()=>e(p)===F.REGISTER);function S(){return k(this,null,function*(){const f=yield h();f&&console.log(f)})}return(f, o)=>C.value?(D(),$("div",M,[a(P,{class:"enter-x"}),a(e(w),{class:"p-4 enter-x",model:s,rules:e(d),ref_key:"formRef",ref:m},{default:n(()=>[a(e(r),{name:"account",class:"enter-x"},{default:n(()=>[a(e(g),{class:"fix-auto-fill",size:"large",value:s.account,"onUpdate:value":o[0]||(o[0]= l=>s.account=l),placeholder:e(t)("sys.login.userName")},null,8,["value","placeholder"])]),_:1}),a(e(r),{name:"mobile",class:"enter-x"},{default:n(()=>[a(e(g),{size:"large",value:s.mobile,"onUpdate:value":o[1]||(o[1]= l=>s.mobile=l),placeholder:e(t)("sys.login.mobile"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1}),a(e(r),{name:"sms",class:"enter-x"},{default:n(()=>[a(e(U),{size:"large",class:"fix-auto-fill",value:s.sms,"onUpdate:value":o[2]||(o[2]= l=>s.sms=l),placeholder:e(t)("sys.login.smsCode")},null,8,["value","placeholder"])]),_:1}),a(e(r),{name:"password",class:"enter-x"},{default:n(()=>[a(e(L),{size:"large",value:s.password,"onUpdate:value":o[3]||(o[3]= l=>s.password=l),placeholder:e(t)("sys.login.password")},null,8,["value","placeholder"])]),_:1}),a(e(r),{name:"confirmPassword",class:"enter-x"},{default:n(()=>[a(e(i),{size:"large",visibilityToggle:"",value:s.confirmPassword,"onUpdate:value":o[4]||(o[4]= l=>s.confirmPassword=l),placeholder:e(t)("sys.login.confirmPassword")},null,8,["value","placeholder"])]),_:1}),a(e(r),{class:"enter-x",name:"policy"},{default:n(()=>[a(e(E),{checked:s.policy,"onUpdate:checked":o[5]||(o[5]= l=>s.policy=l),size:"small"},{default:n(()=>[v(y(e(t)("sys.login.policy")),1)]),_:1},8,["checked"])]),_:1}),a(e(_),{type:"primary",class:"enter-x",size:"large",block:"",onClick:S,loading:u.value},{default:n(()=>[v(y(e(t)("sys.login.registerButton")),1)]),_:1},8,["loading"]),a(e(_),{size:"large",block:"",class:"mt-4 enter-x",onClick:e(c)},{default:n(()=>[v(y(e(t)("sys.login.backSignIn")),1)]),_:1},8,["onClick"])]),_:1},8,["model","rules"])])):G("",!0)}});export{O as default};