import{u as r,d,_ as l}from"./index.js";import{b as c}from"./index-792b1951.js";import{d as p,e as m,a7 as f,_ as h,$ as u,a0 as _,a1 as g,f as C,ac as b,a2 as y}from"./vue-50936e44.js";import{ag as v}from"./antd-685f22a1.js";import"./index-fd2c8e81.js";import"./index-929a8618.js";import"./useContentViewHeight-85a83bf4.js";import"./useWindowSizeFn-7036f6b8.js";import"./uniqBy-401d319d.js";import"./lock-012d98e6.js";const S=p({name:"SwitchItem",components:{Switch:v},props:{event:{type:Number},disabled:{type:Boolean},title:{type:String},def:{type:Boolean}},setup(e){const{prefixCls:t}=r("setting-switch-item"),{t:n}=d(),a=m(()=>e.def?{checked:e.def}:{});function o(s){e.event&&c(e.event,s)}return{prefixCls:t,t:n,handleChange:o,getBindValue:a}}});function k(e,t,n,a,o,s){const i=f("Switch");return h(),u("div",{class:y(e.prefixCls)},[_("span",null,g(e.title),1),C(i,b(e.getBindValue,{onChange:e.handleChange,disabled:e.disabled,checkedChildren:e.t("layout.setting.on"),unCheckedChildren:e.t("layout.setting.off")}),null,16,["onChange","disabled","checkedChildren","unCheckedChildren"])],2)}const P=l(S,[["render",k],["__scopeId","data-v-0f0bf616"]]);export{P as default};
