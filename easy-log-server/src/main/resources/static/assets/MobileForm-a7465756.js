var _=(g,l,o)=>new Promise((c,i)=>{var d=a=>{try{n(o.next(a))}catch(r){i(r)}},m=a=>{try{n(o.throw(a))}catch(r){i(r)}},n=a=>a.done?c(a.value):Promise.resolve(a.value).then(d,m);n((o=o.apply(g,l)).next())});import{C as F}from "./index-93d3fdab.js";import{u as I,a as h,b as B,L as S,_ as w}from "./LoginFormTitle.vue_vue_type_script_setup_true_lang-64884d13.js";import{d as z}from "./index.js";import{aL as k,Z as E,a1 as b}from "./antd-685f22a1.js";import{d as R,k as v,r as V,e as N,u as e,_ as D,$ as M,f as s,a9 as t,E as x,a1 as y,ab as U}from "./vue-50936e44.js";const $={key:0},G=R({__name:"MobileForm",setup(g){const l=k.Item,{t:o}=z(),{handleBackLogin:c,getLoginState:i}=I(),{getFormRules:d}=h(),m=v(),n=v(!1),a=V({mobile:"",sms:""}),{validForm:r}=B(m),C=N(()=>e(i)===S.MOBILE);function L(){return _(this,null,function*(){const f=yield r();f&&console.log(f)})}return(f, u)=>C.value?(D(),M("div",$,[s(w,{class:"enter-x"}),s(e(k),{class:"p-4 enter-x",model:a,rules:e(d),ref_key:"formRef",ref:m},{default:t(()=>[s(e(l),{name:"mobile",class:"enter-x"},{default:t(()=>[s(e(E),{size:"large",value:a.mobile,"onUpdate:value":u[0]||(u[0]= p=>a.mobile=p),placeholder:e(o)("sys.login.mobile"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1}),s(e(l),{name:"sms",class:"enter-x"},{default:t(()=>[s(e(F),{size:"large",class:"fix-auto-fill",value:a.sms,"onUpdate:value":u[1]||(u[1]= p=>a.sms=p),placeholder:e(o)("sys.login.smsCode")},null,8,["value","placeholder"])]),_:1}),s(e(l),{class:"enter-x"},{default:t(()=>[s(e(b),{type:"primary",size:"large",block:"",onClick:L,loading:n.value},{default:t(()=>[x(y(e(o)("sys.login.loginButton")),1)]),_:1},8,["loading"]),s(e(b),{size:"large",block:"",class:"mt-4",onClick:e(c)},{default:t(()=>[x(y(e(o)("sys.login.backSignIn")),1)]),_:1},8,["onClick"])]),_:1})]),_:1},8,["model","rules"])])):U("",!0)}});export{G as default};
