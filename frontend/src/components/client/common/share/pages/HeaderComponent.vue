<template>
  <div
    id="header"
    :class="{
      'on-home': onHome,
      'out-home': !onHome,
      sticky: currentPage !== 'storeDetailPage',
      'on-modal': onModal,
    }"
  >
    <div class="header_top">
      <div class="header_top_left">
        <router-link to="/" @click="goHome('homePage')">
          <img
            class="main-logo"
            src="@/assets/images/JoomakLogo_main.svg"
            alt="JoomakLogo"
          />
        </router-link>
      </div>
      <div class="header_top_middle">
        <div v-if="onAddressConfigRequestModal" class="alert-arrow_wrap">
          <img src="@/assets/images/icon_arrow-right.svg" alt="arrow-right" />
        </div>
        <address-config></address-config>
        <address-config-request-modal></address-config-request-modal>
      </div>
      <div class="header_top_right">
        <router-link
          :to="`/member/${onLogin ? 'logout' : 'login'}`"
          class="header_top_right-elem"
          @click="outStoreListPage('loginPage')"
          :class="{ 'on-click-nav-li': currentPage === 'loginPage' }"
        >
          {{ onLogin ? "LogOut" : "LogIn" }}
        </router-link>
        <span>|</span>
        <router-link
          to="/order-status"
          class="header_top_right-elem"
          @click="outStoreListPage('orderStatusPage')"
          :class="{ 'on-click-nav-li': currentPage === 'orderStatusPage' }"
          >주문 현황</router-link
        >
        <span>|</span>
        <router-link
          v-if="currentAddressObj"
          to="/cart"
          class="header_top_right-elem"
          @click="outStoreListPage('cartPage')"
          :class="{ 'on-click-nav-li': currentPage === 'cartPage' }"
          >장바구니</router-link
        >
        <!-- fake link -->
        <a
          v-else-if="!currentAddressObj"
          to="/cart"
          class="header_top_right-elem"
          @click="checkAddressConfigSelection"
          :class="{ 'on-click-nav-li': currentPage === 'cartPage' }"
          >장바구니</a
        >
      </div>
    </div>
    <div class="header_bottom" v-if="currentAddressObj">
      <!-- v-for로 카테고리 li 반복 -->
      <router-link
        v-for="category in categories"
        :key="category.en"
        :to="`/store-list/${category.en}`"
        @click="onClickCategory(category.en, 'storeListPage')"
        :class="{ 'on-click-nav-li': currentCategory === category.en }"
      >
        {{ category.ko }}
      </router-link>
    </div>
    <!-- fake nav -->
    <div class="header_bottom" v-else-if="!currentAddressObj">
      <a
        v-for="category in categories"
        :key="category.en"
        :to="`/store-list/${category.en}`"
        @click.prevent="onClickCategory(category.en, 'storeListPage')"
        :class="{ 'on-click-nav-li': currentCategory === category.en }"
      >
        {{ category.ko }}
      </a>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";
import {
  SET_ON_ADDRESS_CONFIG_REQUEST_MODAL,
  OPEN_ADDRESS_CONFIG_REQUEST_MODAL,
  CLOSE_ADDRESS_CONFIG_REQUEST_MODAL,
  TOGGLE_ON_HOME,
  SET_CURRENT_PAGE,
  SET_CURRENT_HOME_COORDS,
} from "@/store/modules/common.js";
import { SET_CURRENT_CATEGORY } from "@/store/modules/product.js";
import AddressConfig from "../components/AddressConfig.vue";
import AddressConfigRequestModal from "@/components/client/common/share/pages/AddressConfigRequestModal.vue";

export default {
  components: {
    AddressConfig,
    AddressConfigRequestModal,
  },
  computed: {
    ...mapState("common", [
      "onHome",
      "onLogin",
      "currentPage",
      "onModal",
      "onAddressConfigRequestModal",
    ]),
    ...mapState("product", ["categories", "currentCategory"]),
    ...mapGetters("member", ["currentAddressObj"]),
  },
  methods: {
    ...mapActions("common", [
      `${OPEN_ADDRESS_CONFIG_REQUEST_MODAL}`,
      `${CLOSE_ADDRESS_CONFIG_REQUEST_MODAL}`,
    ]),
    inHome() {
      if (!this.onHome) {
        this.$store.commit(`common/${TOGGLE_ON_HOME}`);
      }
    },
    outHome() {
      if (this.onHome) {
        this.$store.commit(`common/${TOGGLE_ON_HOME}`);
      }
    },
    setCategory(category) {
      this.$store.commit(`product/${SET_CURRENT_CATEGORY}`, category);
    },
    onClickCategory(category, pageName) {
      if (!this.currentAddressObj) {
        this.$store.commit(
          `common/${SET_ON_ADDRESS_CONFIG_REQUEST_MODAL}`,
          true
        );
        return;
      }

      if (this.currentPage !== "storeListPage") {
        this.$store.commit(`common/${SET_CURRENT_PAGE}`, pageName);
      }
      this.outHome();
      this.setCategory(category);
    },

    initializeCategory() {
      this.$store.commit(`product/${SET_CURRENT_CATEGORY}`, "");
    },

    goHome(pageName) {
      if (this.currentPage !== "homePage") {
        this.$store.commit(`common/${SET_CURRENT_HOME_COORDS}`, "morning");
      }
      this.$store.commit(`common/${SET_CURRENT_PAGE}`, pageName);
      this.inHome();
      this.initializeCategory();
      if (window.scrollY !== 0) {
        scrollTo(0, 0);
      }
    },
    outStoreListPage(pageName) {
      this.$store.commit(`common/${SET_CURRENT_PAGE}`, pageName);
      this.outHome();
      this.initializeCategory();
    },

    checkAddressConfigSelection() {
      if (!this.currentAddressObj) {
        this.$store.commit(
          `common/${SET_ON_ADDRESS_CONFIG_REQUEST_MODAL}`,
          true
        );
        return;
      }
    },

    setCurrentCategoryAtLoad() {
      console.log(this.$route.name);
      if (
        this.$route.name !== "homePage" &&
        this.$route.name !== "memberPage" &&
        this.$route.name !== "orderStatusPage"
      ) {
        if (!this.currentAddressObj) {
          this.OPEN_ADDRESS_CONFIG_REQUEST_MODAL();
        } else {
          this.$store.commit(
            `product/${SET_CURRENT_CATEGORY}`,
            this.$route.params.food
          );
        }
      }
    },
  },
  created() {
    // 로드/리로드 이벤트 리스너 추가 : currentCategory 설정
    window.addEventListener("load", this.setCurrentCategoryAtLoad);
  },
  updated() {
    // console.log("Header 업데이트됨");
  },
  beforeUnmount() {
    // 로드/리로드 이벤트 리스너 해제 : currentCategory 설정
    window.removeEventListener("load", this.setCurrentCategoryAtLoad);
  },
};
</script>

<style scoped>
#header {
  position: relative;
  z-index: 30;
  box-sizing: border-box;
  padding-top: 2vh;
  padding-bottom: 0.5vh;
  text-decoration: none;
  width: 100%;
  height: 12vh;
  background-color: #ffc463;
  box-shadow: 0px 8px 10px rgba(0, 0, 0, 0.12);
}
#header.on-home {
  background-color: transparent;
  box-shadow: none;
}
#header.sticky {
  position: fixed;
  top: 0;
  left: 0;
}
#header.on-modal {
  z-index: 10;
}
#header a {
  text-decoration: none;
}
.on-home,
.on-home a {
  color: white;
}
.out-home,
.out-home a {
  color: #9e9e9e;
}
.header_top {
  margin-left: 100px;
  margin-right: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 5vh;
}
.header_top_left,
.header_top_middle,
.header_top_right {
  width: 33vw;
}
.main-logo {
  width: 65px;
  height: auto;
}
.header_top_middle {
  position: relative;
}
.header_top_right {
  display: flex;
  justify-content: flex-end;
}
.header_top_right > span {
  color: #b1b1b1;
}
a.header_top_right-elem {
  color: #999999;
  margin-left: 2.5vh;
  margin-right: 2.5vh;
}
a.header_top_right-elem:hover {
  color: white;
  font-weight: bold;
}
.header_top_right-elem:first-child {
  margin-left: 0px;
}
.header_top_right-elem:last-child {
  margin-right: 0px;
}
.header_top_right a {
  cursor: pointer;
}

.header_bottom {
  box-sizing: border-box;
  margin-left: 15vw;
  margin-right: 15vw;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  height: 4.5vh;
}
.header_bottom > a {
  font-size: 16px;
  cursor: pointer;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 10%;
  height: 100%;
  margin-left: 1vh;
  margin-right: 1vh;
}
.header_bottom > a:hover {
  color: white;
  font-weight: bold;
}
.header_bottom > a:hover::after {
  position: absolute;
  content: "";
  width: 60%;
  height: 4px;
  background-color: white;
  bottom: -0.5vh;
}

a.on-click-nav-li {
  color: white;
  font-weight: bold;
}
.header_bottom .on-click-nav-li::after {
  position: absolute;
  content: "";
  width: 60%;
  height: 4px;
  background-color: white;
  bottom: -0.5vh;
}
.header_bottom > a:hover::after {
  position: absolute;
  content: "";
  width: 60%;
  height: 4px;
  background-color: white;
  bottom: -0.5vh;
}
.header_bottom a:nth-last-child(2) {
  width: 17%;
}
</style>
