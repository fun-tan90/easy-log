var k=(g,l,s)=>new Promise((c,i)=>{var f=a=>{try{n(s.next(a))}catch(r){i(r)}},m=a=>{try{n(s.throw(a))}catch(r){i(r)}},n=a=>a.done?c(a.value):Promise.resolve(a.value).then(f,m);n((s=s.apply(g,l)).next())});import{a as I,c as L,w as _,D as h,f as B,k as e,o as S,h as w,n as o,H as t,I as z,K as x,J as y,t as b,M as R,l as V}from"./index.c2fb29d9.js";import{F as v}from"./index.8c7aee6a.js";import"./index.140cefd0.js";import{C as D}from"./index.66476e28.js";import{u as E,a as N,L as M,_ as U,b as H}from"./LoginFormTitle.5e351425.js";import"./get.4aff8b30.js";import"./useSize.be05b693.js";const G=I({setup(g){const l=v.Item,{t:s}=L(),{handleBackLogin:c,getLoginState:i}=E(),{getFormRules:f}=N(),m=_(),n=_(!1),a=h({mobile:"",sms:""}),{validForm:r}=H(m),C=B(()=>e(i)===M.MOBILE);function F(){return k(this,null,function*(){const d=yield r()})}return(d,u)=>e(C)?(S(),w(R,{key:0},[o(U,{class:"enter-x"}),o(e(v),{class:"p-4 enter-x",model:e(a),rules:e(f),ref_key:"formRef",ref:m},{default:t(()=>[o(e(l),{name:"mobile",class:"enter-x"},{default:t(()=>[o(e(z),{size:"large",value:e(a).mobile,"onUpdate:value":u[0]||(u[0]=p=>e(a).mobile=p),placeholder:e(s)("sys.login.mobile"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1}),o(e(l),{name:"sms",class:"enter-x"},{default:t(()=>[o(e(D),{size:"large",class:"fix-auto-fill",value:e(a).sms,"onUpdate:value":u[1]||(u[1]=p=>e(a).sms=p),placeholder:e(s)("sys.login.smsCode")},null,8,["value","placeholder"])]),_:1}),o(e(l),{class:"enter-x"},{default:t(()=>[o(e(x),{type:"primary",size:"large",block:"",onClick:F,loading:n.value},{default:t(()=>[y(b(e(s)("sys.login.loginButton")),1)]),_:1},8,["loading"]),o(e(x),{size:"large",block:"",class:"mt-4",onClick:e(c)},{default:t(()=>[y(b(e(s)("sys.login.backSignIn")),1)]),_:1},8,["onClick"])]),_:1})]),_:1},8,["model","rules"])],64)):V("",!0)}});export{G as default};