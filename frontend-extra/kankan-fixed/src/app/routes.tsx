import { createBrowserRouter } from "react-router";
import { LandingPage } from "./pages/LandingPage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { OrganizationRegisterPage } from "./pages/OrganizationRegisterPage";
import { BoardPage } from "./pages/BoardPage";
import { AboutPage } from "./pages/AboutPage";

export const router = createBrowserRouter([
  { path: "/", Component: LandingPage },
  { path: "/login", Component: LoginPage },
  { path: "/register", Component: RegisterPage },
  { path: "/register/organization", Component: OrganizationRegisterPage },
  { path: "/board", Component: BoardPage },
  { path: "/about", Component: AboutPage },
]);
