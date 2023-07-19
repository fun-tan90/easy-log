import{aO as $,aF as K,a as V,a4 as t,r as j,bv as D,a7 as S,w as _,f as b,x as U,v as A,a0 as E,a1 as H,z as s,n as r,a3 as y,a$ as L,az as R,b4 as B,cv as W,b0 as I,R as q,b as G,S as Y,o as J,h as Q,i as X,t as Z,Y as ee,q as ne,c as ae}from"./index.c2fb29d9.js";import{b as te}from"./index.ff5a7ad4.js";import"./index.fed83de8.js";import"./useContentViewHeight.ede91aa8.js";import"./index.a038765d.js";import"./ArrowLeftOutlined.74ede690.js";var ce=K("small","default"),le=function(){return{id:String,prefixCls:String,size:t.oneOf(ce),disabled:{type:Boolean,default:void 0},checkedChildren:t.any,unCheckedChildren:t.any,tabindex:t.oneOfType([t.string,t.number]),autofocus:{type:Boolean,default:void 0},loading:{type:Boolean,default:void 0},checked:t.oneOfType([t.string,t.number,t.looseBool]),checkedValue:t.oneOfType([t.string,t.number,t.looseBool]).def(!0),unCheckedValue:t.oneOfType([t.string,t.number,t.looseBool]).def(!1),onChange:{type:Function},onClick:{type:Function},onKeydown:{type:Function},onMouseup:{type:Function},"onUpdate:checked":{type:Function},onBlur:Function,onFocus:Function}},oe=V({name:"ASwitch",__ANT_SWITCH:!0,inheritAttrs:!1,props:le(),slots:["checkedChildren","unCheckedChildren"],setup:function(e,u){var o=u.attrs,h=u.slots,v=u.expose,d=u.emit,p=j();D(function(){S(!("defaultChecked"in o),"Switch","'defaultChecked' is deprecated, please use 'v-model:checked'"),S(!("value"in o),"Switch","`value` is not validate prop, do you mean `checked`?")});var C=_(e.checked!==void 0?e.checked:o.defaultChecked),k=b(function(){return C.value===e.checkedValue});U(function(){return e.checked},function(){C.value=e.checked});var m=A("switch",e),l=m.prefixCls,x=m.direction,F=m.size,f=_(),w=function(){var n;(n=f.value)===null||n===void 0||n.focus()},T=function(){var n;(n=f.value)===null||n===void 0||n.blur()};v({focus:w,blur:T}),E(function(){H(function(){e.autofocus&&!e.disabled&&f.value.focus()})});var g=function(n,i){e.disabled||(d("update:checked",n),d("change",n,i),p.onFieldChange())},z=function(n){d("blur",n)},N=function(n){w();var i=k.value?e.unCheckedValue:e.checkedValue;g(i,n),d("click",i,n)},O=function(n){n.keyCode===I.LEFT?g(e.unCheckedValue,n):n.keyCode===I.RIGHT&&g(e.checkedValue,n),d("keydown",n)},M=function(n){var i;(i=f.value)===null||i===void 0||i.blur(),d("mouseup",n)},P=b(function(){var a;return a={},s(a,"".concat(l.value,"-small"),F.value==="small"),s(a,"".concat(l.value,"-loading"),e.loading),s(a,"".concat(l.value,"-checked"),k.value),s(a,"".concat(l.value,"-disabled"),e.disabled),s(a,l.value,!0),s(a,"".concat(l.value,"-rtl"),x.value==="rtl"),a});return function(){var a;return r(W,{insertExtraNode:!0},{default:function(){return[r("button",y(y(y({},L(e,["prefixCls","checkedChildren","unCheckedChildren","checked","autofocus","checkedValue","unCheckedValue","id","onChange","onUpdate:checked"])),o),{},{id:(a=e.id)!==null&&a!==void 0?a:p.id.value,onKeydown:O,onClick:N,onBlur:z,onMouseup:M,type:"button",role:"switch","aria-checked":C.value,disabled:e.disabled||e.loading,class:[o.class,P.value],ref:f}),[r("div",{class:"".concat(l.value,"-handle")},[e.loading?r(R,{class:"".concat(l.value,"-loading-icon")},null):null]),r("span",{class:"".concat(l.value,"-inner")},[k.value?B(h,e,"checkedChildren"):B(h,e,"unCheckedChildren")])])]}})}}}),de=$(oe);const ie=V({name:"SwitchItem",components:{Switch:de},props:{event:{type:Number},disabled:{type:Boolean},title:{type:String},def:{type:Boolean}},setup(c){const{prefixCls:e}=G("setting-switch-item"),{t:u}=ae(),o=b(()=>c.def?{checked:c.def}:{});function h(v){c.event&&te(c.event,v)}return{prefixCls:e,t:u,handleChange:h,getBindValue:o}}});function ue(c,e,u,o,h,v){const d=Y("Switch");return J(),Q("div",{class:ne(c.prefixCls)},[X("span",null,Z(c.title),1),r(d,ee(c.getBindValue,{onChange:c.handleChange,disabled:c.disabled,checkedChildren:c.t("layout.setting.on"),unCheckedChildren:c.t("layout.setting.off")}),null,16,["onChange","disabled","checkedChildren","unCheckedChildren"])],2)}var ke=q(ie,[["render",ue],["__scopeId","data-v-474ce05a"]]);export{ke as default};
