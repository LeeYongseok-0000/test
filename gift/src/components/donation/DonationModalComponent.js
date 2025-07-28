import React, { useEffect, useState } from "react";
import useCustomLogin from "../../hooks/useCustomLogin";
import { getCookie } from "../../util/cookieUtil";
import axios from "axios";
import { API_SERVER_HOST } from "../../api/backendApi";

function DonationModalComponent({ isOpen, onClose }) {
  const { loginState, exceptionHandle, moveToLogin, token, isLogin } = useCustomLogin();
  const [brand, setBrand] = useState("");
  const [pname, setPname] = useState("");
  const [price, setPrice] = useState("");

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // 개발 및 디버깅을 위해 토큰 값을 확인하는 로그를 추가
  useEffect(() => {
    if (!token) {
      console.warn(
        "경고: 액세스 토큰이 존재하지 않습니다. 로그인 상태를 확인하세요."
      );
      console.log("member 쿠키:", getCookie("member"));
    } else {
      console.log("현재 사용 중인 토큰:", token.substring(0, 30) + "..."); // 토큰의 일부만 출력하여 보안 유지
    }
  }, [token]);



  const handleDonate = async () => {
    setError("");
    setMessage("");

    // 로그인 상태 확인
    if (!isLogin || !token) {
      setError("로그인이 필요합니다.");
      moveToLogin();
      return;
    }

    if (!brand || !pname || !price) {
      setError("모든 필드를 입력해주세요.");
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post(`${API_SERVER_HOST}/api/donations`, {
        amount: parseInt(price, 10),
        count: 1,
        brand,
        pname,
      });

      // ✅ 응답이 성공했을 때의 로직
      setMessage(
        "기부가 완료되었습니다! 마이페이지에서 내역을 확인할 수 있습니다."
      );
      setBrand("");
      setPname("");
      setPrice("");
      console.log(response.data);
      return; // 성공했으므로 함수 종료
    } catch (e) {
      console.error("기부 오류:", e);
      
      // 임시로 모든 오류를 일반 오류로 처리 (테스트용)
      setError(e.response?.data?.message || e.message || "알 수 없는 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: "rgba(0,0,0,0.5)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 1000,
      }}
    >
      <div
        style={{
          backgroundColor: "white",
          padding: "20px",
          borderRadius: "8px",
          width: "400px",
          boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
        }}
      >
        <h2>기부하기</h2>

        <label>
          브랜드
          <input
            type="text"
            value={brand}
            onChange={(e) => setBrand(e.target.value)}
            placeholder="브랜드명 입력"
            style={{ width: "100%", marginBottom: "10px" }}
          />
        </label>

        <label>
          상품 이름
          <input
            type="text"
            value={pname}
            onChange={(e) => setPname(e.target.value)}
            placeholder="상품명 입력"
            style={{ width: "100%", marginBottom: "10px" }}
          />
        </label>

        <label>
          가격 (원)
          <input
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            placeholder="가격 입력"
            style={{ width: "100%", marginBottom: "10px" }}
          />
        </label>

        {error && <p style={{ color: "red" }}>{error}</p>}
        {message && <p style={{ color: "green" }}>{message}</p>}

        <div
          style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}
        >
          <button onClick={handleClose} disabled={loading}>
            닫기
          </button>
          <button onClick={handleDonate} disabled={loading}>
            {loading ? "기부중..." : "기부하기"}
          </button>
        </div>
      </div>
    </div>
  );
}

export default DonationModalComponent;
