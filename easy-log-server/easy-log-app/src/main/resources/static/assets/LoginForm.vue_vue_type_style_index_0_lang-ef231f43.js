var k=(f,p,u)=>new Promise((y,r)=>{var _=n=>{try{m(u.next(n))}catch(g){r(g)}},t=n=>{try{m(u.throw(n))}catch(g){r(g)}},m=n=>n.done?y(n.value):Promise.resolve(n.value).then(_,t);m((u=u.apply(f,p)).next())});import{d as T,k as d,r as U,e as $,u as e,o as P,_ as H,$ as O,t as M,A as K,f as s,a9 as o,a0 as v,E as N,a1 as z,an as q,F as G}from"./vue-50936e44.js";import{u as Z,a as j,b as J,L as Q,_ as W}from"./LoginFormTitle.vue_vue_type_script_setup_true_lang-64884d13.js";import{H as X,u as Y,au as ee,d as ae,aI as se}from"./index.js";import{aL as B,Z as C,ah as te,a1 as oe,aN as ne,aO as le,a4 as re}from"./antd-685f22a1.js";function ce(f,p="modal"){return X.post({url:"/captcha",params:f},{errorMessageMode:p})}const ie={class:"captcha-row"},ue={class:"captcha-row-control"},de={class:"captcha-row-img"},pe=["src","alt"],ye=T({__name:"LoginForm",setup(f){const p=ne,u=le,y=re,r=B.Item,_=C.Password,{t}=ae(),{notification:m,createErrorModal:n}=se(),{prefixCls:g}=Y("login"),D=ee(),{getLoginState:R}=Z(),{getFormRules:A}=j(),I=d(),w=d(!1),x=d(!1),L=d(""),b=d(""),h=d(!1),c=U({username:"admin",password:"123456",captchaValue:""}),{validForm:E}=J(I),F=$(()=>e(R)===Q.LOGIN);P(()=>{S()});function S(){return k(this,null,function*(){try{h.value=!0;const l=new Date().getTime().toString().substring(1);L.value=l;const a={captchaKey:l,width:103,height:38,len:2,survival:5};b.value=yield ce(a,"none"),h.value=!1}catch(l){h.value=!1}})}function V(){return k(this,null,function*(){const l=yield E();if(l)try{w.value=!0;const a=yield D.login({password:l.password,username:l.username,captchaKey:L.value,captchaValue:l.captchaValue,rememberMe:x.value,mode:"none"});a&&m.success({message:t("sys.login.loginSuccessTitle"),description:`${t("sys.login.loginSuccessDesc")}: ${a.userName}`,duration:3})}catch(a){yield S(),n({title:t("sys.api.errorTip"),content:a.message||t("sys.api.networkExceptionMsg"),getContainer:()=>document.body.querySelector(`.${g}`)||document.body})}finally{w.value=!1}})}return(l,a)=>(H(),O(G,null,[M(s(W,{class:"enter-x"},null,512),[[K,F.value]]),M(s(e(B),{class:"p-4 enter-x",model:c,rules:e(A),ref_key:"formRef",ref:I,onKeypress:q(V,["enter"])},{default:o(()=>[s(e(r),{name:"username",class:"enter-x"},{default:o(()=>[s(e(C),{size:"large",value:c.username,"onUpdate:value":a[0]||(a[0]=i=>c.username=i),placeholder:e(t)("sys.login.userName"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1}),s(e(r),{name:"password",class:"enter-x"},{default:o(()=>[s(e(_),{size:"large",visibilityToggle:"",value:c.password,"onUpdate:value":a[1]||(a[1]=i=>c.password=i),placeholder:e(t)("sys.login.password")},null,8,["value","placeholder"])]),_:1}),v("div",ie,[v("div",ue,[s(e(r),{name:"captchaValue",class:"enter-x"},{default:o(()=>[s(e(C),{size:"large",value:c.captchaValue,"onUpdate:value":a[2]||(a[2]=i=>c.captchaValue=i),placeholder:e(t)("sys.login.smsPlaceholder"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1})]),v("div",de,[s(e(y),{size:"small",spinning:h.value},{default:o(()=>[v("img",{src:b.value,alt:e(t)("sys.login.smsHandler"),onClick:S},null,8,pe)]),_:1},8,["spinning"])])]),s(e(u),{class:"enter-x"},{default:o(()=>[s(e(p),{span:12},{default:o(()=>[s(e(r),null,{default:o(()=>[s(e(te),{checked:x.value,"onUpdate:checked":a[3]||(a[3]=i=>x.value=i),size:"small"},{default:o(()=>[N(z(e(t)("sys.login.rememberMe")),1)]),_:1},8,["checked"])]),_:1})]),_:1})]),_:1}),s(e(r),{class:"enter-x"},{default:o(()=>[s(e(oe),{type:"primary",size:"large",block:"",onClick:V,loading:w.value},{default:o(()=>[N(z(e(t)("sys.login.loginButton")),1)]),_:1},8,["loading"])]),_:1})]),_:1},8,["model","rules","onKeypress"]),[[K,F.value]])],64))}});export{ye as _};
