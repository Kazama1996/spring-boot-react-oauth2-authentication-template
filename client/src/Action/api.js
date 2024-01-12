import instance from "./axiosInstance";

const customConfig = {
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
};

export const SendForgotPasswordRequest = (data) => {
  return instance.post(
    "/api/v1/auth/public/forgotPassword",
    data,
    customConfig
  );
};

export const SendLoginRequest = (data) => {
  return instance.post(
    "/api/v1/auth/public/authentication",
    data,
    customConfig
  );
};

export const SendRegisterRequest = (data) => {
  return instance.post("/api/v1/auth/public/register", data, customConfig);
};

export const SendUpdatePasswordRequest = (data) => {
  return instance.patch("/api/v1/auth/public/password", data, customConfig);
};

export const GetUserProfile = () => {
  return instance.get("/api/v1/users/private/currentuser", customConfig);
};

export const SendPasswordResetToken = (data) => {
  return instance.post(
    "/api/v1/auth/public/passwordResetToken",
    data,
    customConfig
  );
};
