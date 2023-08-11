import { useState, useCallback } from "react";

export default function Code() {
  const [Code, SetCode] = useState();

  const onChange = useCallback((e) => {
    SetCode(e.target.value);
  }, []);

  return (
    <div className="Div">
      <label className="Label" htmlFor="Code">
        인증코드
      </label>

      <br />

      <input
        className="Input"
        id="Code"
        type="text"
        value={Code}
        onChange={onChange}
        placeholder="인증코드를 입력해주세요"
        maxLength="6"
      />
    </div>
  );
}
